package org.evergreen.web.view.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ErrorsTag extends SimpleTagSupport {

	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException, IOException {
		Map<String, String> errors = (Map<String, String>) getJspContext()
				.getAttribute("org.evergreen.web.ActionContext.errors",
						PageContext.REQUEST_SCOPE);
		if (errors != null) {
			color = color == null ? "red" : color;
			for (String key : errors.keySet()) {
				String msg = "<font color='" + color + "'>" + errors.get(key)
						+ ".</font><br/>";
				getJspContext().getOut().write(msg);
			}
		}
	}

}
