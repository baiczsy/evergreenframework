package org.evergreen.plugin;

import org.evergreen.beans.factory.AnnotationContextFactory;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.web.HandlerFactory;
import org.evergreen.web.FrameworkServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationContextListener implements ServletContextListener{

    private final static String SCAN_PACKAGE = "scanPackage";

    /**
     * 初始化容器
     */
    public void contextInitialized(ServletContextEvent event) {
        String path = event.getServletContext().getInitParameter(SCAN_PACKAGE);
        BeanFactory factory = new AnnotationContextFactory(path);
        HandlerFactory contextFactory = new ContainerHandlerFactory(factory);
        event.getServletContext().setAttribute(FrameworkServlet.HANDLER_FACTORY, contextFactory);
    }

    /**
     * 关闭容器
     */
    public void contextDestroyed(ServletContextEvent event) {
        ContainerHandlerFactory handlerFactory = (ContainerHandlerFactory)event.getServletContext().getAttribute(FrameworkServlet.HANDLER_FACTORY);
        handlerFactory.getBeanFactory().close();
        event.getServletContext().removeAttribute(FrameworkServlet.HANDLER_FACTORY);
    }
}
