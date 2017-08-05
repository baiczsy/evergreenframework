package org.evergreen.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ViewResult {

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext.getContext().get(
				FrameworkServlet.REQUEST);
	}

	protected HttpServletResponse getResponse() {
		return (HttpServletResponse) ActionContext.getContext().get(
				FrameworkServlet.RESPONSE);
	}

	protected abstract void execute() throws Exception;
}
