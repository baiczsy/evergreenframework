package org.evergreen.plugin;

import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.utils.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.HandlerFactory;
import org.evergreen.web.exception.ActionException;

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
        String beanName = BeanNameUtil.getBeanName(definition.getActionClass());
        return beanFactory.getBean(beanName);
    }
}
