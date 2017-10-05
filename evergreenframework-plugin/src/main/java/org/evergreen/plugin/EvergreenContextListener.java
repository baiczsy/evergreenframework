package org.evergreen.plugin;

import org.evergreen.beans.factory.AnnotationContextFactory;
import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.web.ActionFactory;
import org.evergreen.web.FrameworkServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class EvergreenContextListener implements ServletContextListener{

    private final static String SCAN_PACKAGE = "scanPackage";

    /**
     * 初始化容器
     */
    public void contextInitialized(ServletContextEvent event) {
        String path = event.getServletContext().getInitParameter(SCAN_PACKAGE);
        BeanFactory factory = new AnnotationContextFactory(path);
        ActionFactory contextFactory = new EvergreenContextFactory(factory);
        event.getServletContext().setAttribute(FrameworkServlet.ACTION_FACTORY, contextFactory);
    }

    /**
     * 关闭容器
     */
    public void contextDestroyed(ServletContextEvent event) {
        EvergreenContextFactory contextFactory = (EvergreenContextFactory)event.getServletContext().getAttribute(FrameworkServlet.ACTION_FACTORY);
        contextFactory.getBeanFactory().close();
        event.getServletContext().removeAttribute(FrameworkServlet.ACTION_FACTORY);
    }
}
