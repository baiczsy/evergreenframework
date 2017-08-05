package org.evergreen.web.view;

import org.evergreen.web.ViewResult;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class DefaultViewResult extends ViewResult {
	
	final static String CONTENT_TYPE = "text/html;charset=utf-8";
	
	private Object returnVal;
	
	public DefaultViewResult(Object returnVal){
		this.returnVal = returnVal;
	}

	@Override
	protected void execute() throws IOException {
		HttpServletResponse response = getResponse();
		response.setContentType(CONTENT_TYPE);
		response.getWriter().println(returnVal);
		
	}

}
