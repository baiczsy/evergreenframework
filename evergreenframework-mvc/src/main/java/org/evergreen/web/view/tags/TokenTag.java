package org.evergreen.web.view.tags;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class TokenTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException {
		String token = UUID.randomUUID().toString();
		getJspContext().setAttribute(
				"org.evergreen.web.ActionContext.token",
				token,PageContext.REQUEST_SCOPE);
		getJspContext().setAttribute(
				"org.evergreen.web.ActionContext.token",
				token,PageContext.SESSION_SCOPE);
		getJspContext().getOut().write(
				"<input type='hidden' name='org.evergreen.web.ActionContext.token' value='"
						+ token + "'/>");
	}

}
