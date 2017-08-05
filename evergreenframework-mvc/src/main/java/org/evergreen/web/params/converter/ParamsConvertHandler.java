package org.evergreen.web.params.converter;

import org.evergreen.web.ActionContext;
import org.evergreen.web.FrameworkServlet;
import org.evergreen.web.params.ParamInfo;

import javax.servlet.http.HttpServletRequest;

public abstract class ParamsConvertHandler {

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) ActionContext
				.getContext().get(FrameworkServlet.REQUEST);
		
	}

	public abstract Object execute(ParamInfo paramInfo) throws Exception;
}
