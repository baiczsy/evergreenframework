package org.evergreen.web.view;

import java.io.IOException;

import javax.servlet.ServletException;

public class ForwardView extends JumpPage {
	
	public ForwardView() {		
	}

	public ForwardView(String url) {
		this.url = url;
	}

	protected void execute() throws IOException, ServletException {
		if (url != null) {
			getRequest().getRequestDispatcher("/" + url.trim()).forward(getRequest(),
					getResponse());
		}
	}
}
