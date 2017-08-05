package org.evergreen.plugin;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.factory.EvergreenContainerFactory;

/**
 * 初始化IOC容器
 * 
 * @author wangl
 * 
 */
public class EvergreenInitListener implements ServletContextListener {

	/**
	 * 执行初始化命令
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		new InitCommandInvoker(servletContext).doExecute();		
	}

	/**
	 * 关闭容器
	 */
	public void contextDestroyed(ServletContextEvent event) {
		BeanFactory factory = (BeanFactory)event.getServletContext().getAttribute(EvergreenContainerFactory.BEAN_FACTORY);
		factory.close();
		event.getServletContext().removeAttribute(EvergreenContainerFactory.BEAN_FACTORY);
	}

}
