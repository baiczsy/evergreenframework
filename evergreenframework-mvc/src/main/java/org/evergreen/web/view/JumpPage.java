package org.evergreen.web.view;


import org.evergreen.web.ViewResult;

public abstract class JumpPage extends ViewResult {

	protected String url;

	public void setUrl(String url) {
		this.url = url;
	}

}
