package org.evergreen.plugin;

import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.evergreen.web.ActionContext;
import org.evergreen.web.ActionFactory;
import org.evergreen.web.FrameworkServlet;

public abstract class ContainerFactory implements ActionFactory{

	/**
	 * 获取ServletContext
	 */
	protected ServletContext getServletContext() {
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(FrameworkServlet.REQUEST);
		return request.getServletContext();
	}

	/**
	 * 获取容器的beanNeam
	 * 
	 * @param method
	 * @return
	 * @throws BeanContainerException
	 */
	protected abstract String getBeanName(Method method) throws BeanContainerException;
}
