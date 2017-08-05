package org.evergreen.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.evergreen.web.ActionContext;
import org.evergreen.web.FrameworkServlet;

public class UrlPatternUtil {

	public static String getUrlPattern(HttpServletRequest request){
		StringBuilder urlPattern = new StringBuilder();
		urlPattern.append(request.getPathInfo() == null ? "" : request.getPathInfo());
		urlPattern.append("".equals(request.getServletPath()) ? "" : request.getServletPath());
		return urlPattern.toString();
	}
}
