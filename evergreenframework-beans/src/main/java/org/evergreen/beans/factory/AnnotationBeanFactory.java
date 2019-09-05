package org.evergreen.beans.factory;

import org.evergreen.beans.annotation.ScopeType;

public class AnnotationBeanFactory extends BeanFactory {

	/**
	 * 让父类根据扫描对象执行扫描初始化的工作
	 * @param path
	 */
	public AnnotationBeanFactory(String path) {
		super(path);
	}

	/**
	 * 获取实例(延迟加载)
	 */
	@Override
	protected Object doGetBean(String beanName) {
		//获取Bean的描述定义
		BeanDefinition definition = getBeanDefinition(beanName);
		//如果是描述中指定的作用域为单例,那么将创建一个Bean实例,然后再放入容器
		if (ScopeType.SINGLETON.equals(definition.getScope())) {
			//如果容器中存在单例的Bean实例,直接从容器中获取,否则创建一个Bean实例放入容器中并返回
			if (beansMap.containsKey(beanName)) {
				return beansMap.get(beanName);
			} else {
				return assemblySingleton(beanName, definition);
			}
		} else {
			//否则以原型的方式装配Bean实例
			return assemblyPrototype(definition);
		}
	}

    /**
     * 装配单例
     * @param beanName
     * @param definition
     * @return
     */
	private Object assemblySingleton(String beanName, BeanDefinition definition){
        synchronized (beansMap) {
            registerSingleton(beanName, definition);
            Object bean = beansMap.get(beanName);
            injectProperty(definition.getBeanClass(), bean);
            return bean;
        }
    }

}
