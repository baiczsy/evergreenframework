package org.evergreen.plugin.factory;

import java.io.IOException;
import java.lang.reflect.Method;

import org.evergreen.plugin.ContainerFactory;
import org.evergreen.plugin.BeanContainerException;
import org.evergreen.plugin.BeanNameUtil;
import org.evergreen.web.ActionDefinition;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.RequestMappingException;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContainerFactory extends ContainerFactory {

	private WebApplicationContext getApplicationContext() {
		return WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
	}

	public Object crateAction(ActionDefinition definition)
			throws IOException, ActionException {
		if (definition == null)
			throw new RequestMappingException();
		Method method = definition.getMethod();
		if (method != null)
			try {
				return getApplicationContext().getBean(getBeanName(method));
			} catch (BeansException e) {
				e.printStackTrace();
				throw new RequestMappingException();
			} catch (BeanContainerException e) {
				e.printStackTrace();
				throw new RequestMappingException();
			}
		return null;
	}

	/**
	 * 获取BeanName,也就是获取容器的key
	 */
	protected String getBeanName(Method method) throws BeanContainerException {
		if (method.getDeclaringClass().isAnnotationPresent(Controller.class)) {
			String value = method.getDeclaringClass()
					.getAnnotation(Controller.class).value();
			if (!"".equals(value))
				return value;
		} 
		if (method.getDeclaringClass().isAnnotationPresent(
				Component.class)) {
			String value = method.getDeclaringClass()
					.getAnnotation(Component.class).value();
			if (!"".equals(value))
				return value;
		} 
		//按照默认的名字查找
		return BeanNameUtil.toLowerBeanName((method.getDeclaringClass()
					.getSimpleName()));
	}

}
