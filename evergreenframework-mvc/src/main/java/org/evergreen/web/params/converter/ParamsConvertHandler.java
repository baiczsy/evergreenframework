package org.evergreen.web.params.converter;

import org.evergreen.web.ActionContext;
import org.evergreen.web.FrameworkServlet;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.params.ParamInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class ParamsConvertHandler {

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext
				.getContext().get(FrameworkServlet.REQUEST);
		
	}

	public abstract Object execute(ParamInfo paramInfo) throws ActionException;
}
