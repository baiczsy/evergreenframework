package org.evergreen.web.params.converter.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.evergreen.web.ActionContext;
import org.evergreen.web.FrameworkServlet;
import org.evergreen.web.params.ParamInfo;
import org.evergreen.web.params.converter.ParamsConvertHandler;

public class ServletApiConvertHandler extends ParamsConvertHandler {

	public Object execute(ParamInfo paramInfo) {
		if (paramInfo.getParamType() == HttpServletRequest.class)
			return getRequest();
		else if (paramInfo.getParamType() == HttpServletResponse.class)
			return ActionContext.getContext().get(FrameworkServlet.RESPONSE);
		else if (paramInfo.getParamType() == HttpSession.class)
			return getRequest().getSession();
		else if (paramInfo.getParamType() == ServletContext.class)
			return getRequest().getServletContext();
		return null;
	}

}
