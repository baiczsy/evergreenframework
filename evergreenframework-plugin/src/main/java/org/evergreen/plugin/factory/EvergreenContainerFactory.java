package org.evergreen.plugin.factory;

import java.io.IOException;
import java.lang.reflect.Method;

import org.evergreen.beans.annotation.Component;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.ContainerFactory;
import org.evergreen.plugin.BeanContainerException;
import org.evergreen.plugin.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.RequestMappingException;

public class EvergreenContainerFactory extends ContainerFactory {
	
	public static final String BEAN_FACTORY = "org.evergreen.container.factory";

	public Object crateAction(ActionDefinition definition)
			throws IOException, ActionException {
		if (definition == null) {
			throw new RequestMappingException();
		}
		BeanFactory factory = (BeanFactory) getServletContext().getAttribute(
				BEAN_FACTORY);
		Method method = definition.getMethod();
		if (method != null)
			try {
				return factory.getBean(getBeanName(method));
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
