package org.evergreen.web.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.evergreen.web.ViewResult;
import org.evergreen.web.utils.JsonUtil;

public class Json extends ViewResult {

	public final static String CONTENT_TYPE = "application/json;charset=utf-8";

	private String json;

	public Json(Object arg) {
		json = JsonUtil.toJson(arg);
	}

	public Json(Object arg, String format) {
		json = JsonUtil.toJson(arg, format);
	}

	public Json(Object arg, String[] keys) {
		json = JsonUtil.toJson(arg, keys);
	}

	public Json(Object arg, String[] keys, String format) {
		json = JsonUtil.toJson(arg, keys, format);
	}

	protected void execute() throws IOException {
		HttpServletResponse response = getResponse();
		response.setContentType(CONTENT_TYPE);
		response.getWriter().println(json);
	}

}
