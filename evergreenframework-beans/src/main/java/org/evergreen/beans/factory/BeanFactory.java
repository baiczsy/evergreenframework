package org.evergreen.beans.factory;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import org.evergreen.beans.annotation.Component;
import org.evergreen.beans.annotation.Scope;
import org.evergreen.beans.annotation.ScopeType;
import org.evergreen.beans.factory.exception.BeanDefinitionException;
import org.evergreen.beans.factory.exception.CreateBeanException;
import org.evergreen.beans.factory.exception.InjectPropertyException;
import org.evergreen.beans.utils.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象bean工厂,也是IOC的核心容器,这个容器包含两个重要的Map集合 一个用于存放Bean的描述定义,一个用于存放实例化的Bean组件
 *
 * @author wnagl
 */
public abstract class BeanFactory {

    /**
     * 存放bean的描述
     */
    final Map<String, BeanDefinition> definitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    /**
     * 存放bean的实例
     */
    final Map<String, Object> beansMap = new ConcurrentHashMap<String, Object>();

    /**
     * 在构造方法中初始化并构建所有bean描述
     * @param path 扫描路径
     */
    public BeanFactory(String path) {
        ClassInfoList classInfoList = ScanUtils.scan(path);
        initDefinitionMap(classInfoList);
    }

    /**
     * 解析所有的类名并并检查容器中是否存在当前的Bean描述,如果不存在,构建Bean描述放入容器
     * @param classInfoList
     */
    private void initDefinitionMap(ClassInfoList classInfoList) {
        for (ClassInfo classInfo : classInfoList) {
            Class<?> beanClass = classInfo.loadClass(true);
            if (beanClass.isAnnotationPresent(Component.class)) {
                String beanName = BeanNameUtil.getBeanName(beanClass);
                if (definitionMap.containsKey(beanName)) {
                    throw new BeanDefinitionException(
                            "conflicts with existing, non-compatible bean definition of same name and class ["
                                    + beanClass + "]");
                } else {
                    definitionMap.put(beanName,
                            createBeanDefinition(beanClass));
                }
            }
        }
    }

    /**
     * 构建bean描述定义,将bean的scope以及类名封装到BeanDefinition中
     * 创建的Bean描述会放入definitionMap的集合中保存
     * Bean的类名作为集合的key,而整个BeanDefinition对象作为value
     *
     * @param beanClass
     */
    private BeanDefinition createBeanDefinition(Class<?> beanClass) {
        // 创建BeanDefinition
        BeanDefinition definition = new BeanDefinition();
        //设置Bean的Class对象
        setBeanClass(definition, beanClass);
        //设置Bean的作用域
        setBeanScope(definition, beanClass);
        //设置Bean是否需要代理
        setBeanProxy(definition, beanClass);
        //设置Bean的初始化方法
        setInitMethods(definition, beanClass);
        //设置Bean的销毁方法
        setDestroyMethods(definition, beanClass);
        return definition;
    }

    /**
     * 为definition设置BeanClass 将当前Bean的class对象保存到描述对象中
     * @param definition
     * @param beanClass
     */
    private void setBeanClass(BeanDefinition definition, Class<?> beanClass) {
        definition.setBeanClass(beanClass);
    }

    /**
     * 为definition设置Bean的作用域
     * 如果bean的class上指定了Scope注解(也就是容器创建Bean的方式),一并保存Bean的作用域 否则Bean的作用默认创建方式将使用单例
     * @param definition
     * @param beanClass
     */
    private void setBeanScope(BeanDefinition definition, Class<?> beanClass) {
        String scope = (beanClass.isAnnotationPresent(Scope.class)) ? beanClass
                .getAnnotation(Scope.class).value() : ScopeType.SINGLETON;
        definition.setScope(scope);
    }

    /**
     * 设置是否创建代理
     * @param definition
     * @param beanClass
     */
    private void setBeanProxy(BeanDefinition definition, Class<?> beanClass) {
        if (ProxyUtil.hasProxyAnnotation()) {
            if (beanClass.isAnnotationPresent(ProxyUtil.getProxyAnnotation())) {
                definition.setProxy(true);
                return;
            }
            for (Method method : beanClass.getMethods()) {
                if (method.isAnnotationPresent(ProxyUtil.getProxyAnnotation())) {
                    definition.setProxy(true);
                    return;
                }
            }
        }
    }

    /**
     * 设置Bean的初始化方法
     * @param definition
     * @param beanClass
     */
    private void setInitMethods(BeanDefinition definition, Class<?> beanClass) {
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                definition.getInitMethods().add(method);
            }
        }
    }

    /**
     * 设置Bean的销毁方法
     * @param definition
     * @param beanClass
     */
    private void setDestroyMethods(BeanDefinition definition, Class<?> beanClass) {
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PreDestroy.class)) {
                definition.getDestroyMethods().add(method);
            }
        }
    }

    /**
     * 将SingleTon的Bean注册到容器中
     * @param beanName
     * @param definition
     */
    void registerSingleton(String beanName, BeanDefinition definition) {
        Object bean = createInstance(definition);
        // 将bean实例放入bean容器中
        beansMap.put(beanName, bean);
    }

    /**
     * 装配原型Bean实例
     */
    protected Object assemblyPrototype(BeanDefinition definition){
        Object bean = createInstance(definition);
        injectProperty(definition.getBeanClass(), bean);
        return bean;
    }

    /**
     * 执行注入,当创建代理后要执行依赖注入,由于JDK代理对象不适用于注入, 则如果是JDK代理
     * 这获取回调处理器InvocationHandler当中的目标对象执行注入
     * @param beanClass
     * @param bean
     */
    protected void injectProperty(Class<?> beanClass, Object bean) {
        try {
            bean = ProxyTargetUtil.getTarget(bean);
            DependencyInvoker.inject(bean, beanClass, this);
        } catch (NoSuchFieldException e) {
            throw new InjectPropertyException("Inject property fail.", e);
        } catch (IllegalAccessException e) {
            throw new InjectPropertyException("Inject property fail.", e);
        }
    }

    /**
     * 根据描述决定是否创建代理对象 创建之后执行对象的初始化方法
     *
     * @param definition
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    protected Object createInstance(BeanDefinition definition) {
        try {
            Object instance = (definition.isProxy()) ? ProxyUtil.createProxy(definition
                    .getBeanClass()) : definition.getBeanClass().newInstance();
            //执行初始化方法
            executeInitMethods(instance, definition);
            return instance;
        } catch (InstantiationException e) {
            throw new CreateBeanException("Create bean instance fail.", e);
        } catch (IllegalAccessException e) {
            throw new CreateBeanException("Create bean instance fail.", e);
        } catch (NoSuchFieldException e) {
            throw new CreateBeanException("Create bean instance fail.", e);
        }
    }

    /**
     * 执行Bean的初始化方法
     * @param instance
     * @param definition
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void executeInitMethods(Object instance, BeanDefinition definition)
            throws NoSuchFieldException, SecurityException,
            IllegalArgumentException, IllegalAccessException {
        instance = ProxyTargetUtil.getTarget(instance);
        for (Method method : definition.getInitMethods()) {
            try {
                method.invoke(instance);
            } catch (Exception e) {
                throw new RuntimeException("Init method callback fail.", e);
            }
        }
    }

    /**
     * 获取Bean描述容器
     *
     * @return
     */
    Map<String, BeanDefinition> getDefinitionMap() {
        return definitionMap;
    }

    /**
     * 关闭容器,关闭前执行销毁方法
     */
    public void close() {
        // 执行销毁方法
        executeDestroyMethods();
        // 清空Bean描述集合
        definitionMap.clear();
        // 清空Bean实例集合
        beansMap.clear();
    }

    /**
     * 执行容器中所有Bean的销毁前方法
     */
    private void executeDestroyMethods() {
        for (String key : definitionMap.keySet()) {
            BeanDefinition definition = definitionMap.get(key);
            try {
                Object instance = beansMap.get(key);
                if (instance != null) {
                    instance = ProxyTargetUtil.getTarget(instance);
                    // 执行销毁前方法
                    for (Method method : definition.getDestroyMethods()) {
                        method.invoke(instance);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Destroy method callback fail.", e);
            }
        }
    }

    /**
     * 依据beanName获取BeanDefinition
     *
     * @param beanName
     * @return
     */
    protected BeanDefinition getBeanDefinition(String beanName) {
        BeanDefinition beanDefinition = definitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeanDefinitionException("Can not find bean by " + beanName);
        }
        return beanDefinition;
    }

    /**
     * 获取bean实例
     *
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return doGetBean(beanName);
    }

    /**
     * 获取bean实例(泛型)
     *
     * @param beanName
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> clazz) {
        return (T) doGetBean(beanName);
    }

    /**
     * 获取bean实例
     *
     * @param beanName
     * @return
     */
    protected abstract Object doGetBean(String beanName);

}
