package org.evergreen.web;

import org.evergreen.web.factory.WebApplicationFactory;

import javax.servlet.ServletContext;

public class ActionFactoryContext {

    private ActionFactory actionFactory;
    private ServletContext servletContext;

    public ActionFactoryContext(ServletContext servletContext){
        this.servletContext = servletContext;
        Object object = servletContext.getAttribute(FrameworkServlet.ACTION_FACTORY);
        if(object != null){
            actionFactory = (ActionFactory)object;
        } else {
            actionFactory = new WebApplicationFactory();
        }
    }

    public void initAttribute(){
        servletContext.setAttribute(FrameworkServlet.ACTION_FACTORY, actionFactory);
    }
}
