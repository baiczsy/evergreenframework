package org.evergreen.plugin;

import org.evergreen.beans.annotation.Component;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.utils.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.ActionFactory;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.TargetActionException;
import java.lang.reflect.Method;

public class EvergreenContextFactory implements ActionFactory {

    private BeanFactory beanFactory;

    public EvergreenContextFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Object crateAction(ActionDefinition definition) throws ActionException {
        Method method = definition.getMethod();
        try {
            String beanName = getBeanName(method);
            return beanFactory.getBean(beanName);
        } catch (BeanContainerException e) {
            throw new TargetActionException("Create target action handler fail.", e);
        }
    }

    protected String getBeanName(Method method) throws BeanContainerException {
        if (method.getDeclaringClass().getAnnotation(Component.class) == null)
            throw new BeanContainerException();
        String beanName = method.getDeclaringClass()
                .getAnnotation(Component.class).value();
        beanName = ("".equals(beanName)) ? BeanNameUtil.toLowerBeanName((method
                .getDeclaringClass().getSimpleName())) : beanName;
        return beanName;
    }
}
