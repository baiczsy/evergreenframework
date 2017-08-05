package org.evergreen.web.view.tags;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.evergreen.web.utils.ResourceUtil;

public class I18nTag extends SimpleTagSupport {

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		Locale locale = (Locale) pageContext.getAttribute(
				"org.evergreen.web.ActionContext.locale",
				PageContext.SESSION_SCOPE);
		locale = (locale == null) ? request.getLocale() : locale;
		String baseName = request.getServletContext().getInitParameter("i18n");
		Map<String, String> resourceMap = ResourceUtil.getResource(baseName,
				locale);
		getJspContext().getOut().write(resourceMap.get(key));
	}

}
