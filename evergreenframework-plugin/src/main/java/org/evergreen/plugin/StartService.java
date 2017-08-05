package org.evergreen.plugin;

import javax.servlet.ServletContext;

/**
 * 容器启动时,可以实现此接口做一些自定义初始化
 * @author lenovo
 *
 */
public interface StartService {
	public void execute(ServletContext servletContext);
}
