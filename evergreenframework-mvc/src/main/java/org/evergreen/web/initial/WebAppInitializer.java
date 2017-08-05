package org.evergreen.web.initial;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface WebAppInitializer {

	void onStartup(ServletContext servletContext) throws ServletException;
}
