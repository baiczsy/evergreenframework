package org.evergreen.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ViewResult {

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext.getContext().get(
				FrameworkServlet.REQUEST);
	}

	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) ActionContext.getContext().get(
				FrameworkServlet.RESPONSE);
	}

	protected abstract void execute() throws IOException, ServletException;
}
