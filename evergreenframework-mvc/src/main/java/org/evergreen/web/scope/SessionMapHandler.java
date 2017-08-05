package org.evergreen.web.scope;

public class SessionMapHandler extends ScopeHandler {

	public SessionMapHandler(Object target) {
		super(target);
	}

	void setAttribute(String key, Object value) {
		getLocalRequest().getSession().setAttribute(key, value);
	}

	Object getAttribute(String key) {
		return getLocalRequest().getSession().getAttribute(key);
	}

	void removeAttribute(String key) {
		getLocalRequest().getSession().removeAttribute(key);
	}

}
