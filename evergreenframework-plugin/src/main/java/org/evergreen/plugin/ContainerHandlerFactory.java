package org.evergreen.plugin;

import org.evergreen.beans.annotation.Component;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.utils.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.HandlerFactory;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.TargetActionException;
import java.lang.reflect.Method;

public class ContainerHandlerFactory implements HandlerFactory {

    private BeanFactory beanFactory;

    public ContainerHandlerFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * 从容器中获取Bean实例
     * @param definition
     * @return
     * @throws ActionException
     */
    public Object crateAction(ActionDefinition definition) throws ActionException {
        Method method = definition.getMethod();
        String beanName = getBeanName(method);
        return beanFactory.getBean(beanName);
    }

    /**
     * 获取@Component注解中的Bean名称
     * @param method
     * @return
     * @throws ActionException
     */
    protected String getBeanName(Method method) throws ActionException{
        if (!method.getDeclaringClass().isAnnotationPresent(Component.class)) {
            throw new TargetActionException("Target action not a container managed bean.");
        }
        String beanName = method.getDeclaringClass()
                .getAnnotation(Component.class).value();
        beanName = ("".equals(beanName)) ? BeanNameUtil.toLowerBeanName((method
                .getDeclaringClass().getSimpleName())) : beanName;
        return beanName;
    }
}
