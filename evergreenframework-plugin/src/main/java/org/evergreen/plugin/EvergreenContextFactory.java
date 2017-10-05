package org.evergreen.plugin;

import org.evergreen.beans.annotation.Component;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.utils.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.ActionFactory;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.RequestMappingException;

import java.io.IOException;
import java.lang.reflect.Method;

public class EvergreenContextFactory implements ActionFactory {

	private BeanFactory beanFactory;

	public EvergreenContextFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public Object crateAction(ActionDefinition definition)
			throws IOException, ActionException {
		if (definition == null) {
			throw new RequestMappingException();
		}
		Method method = definition.getMethod();
		if (method != null)
			try {
				return beanFactory.getBean(getBeanName(method));
			} catch (BeanContainerException e) {
				e.printStackTrace();
				throw new RequestMappingException();
			}
		return null;
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
