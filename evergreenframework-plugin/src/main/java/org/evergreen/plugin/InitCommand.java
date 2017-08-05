package org.evergreen.plugin;

import javax.servlet.ServletContext;

public interface InitCommand {
	public void execute(ServletContext servletContext);
}
