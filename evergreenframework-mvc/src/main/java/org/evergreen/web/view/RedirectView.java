package org.evergreen.web.view;

import java.io.IOException;

import org.evergreen.web.ActionContext;

public class RedirectView extends JumpPage {
	
	public RedirectView(){
	}

	public RedirectView(String url) {
		this.url = url;
	}

	protected void execute() throws IOException {
		if (url != null)
			getResponse().sendRedirect(
					ActionContext.getContext().getContextPath() + "/"
							+ url.trim());

	}

}
