package org.evergreen.plugin;

import java.util.ServiceLoader;

import javax.servlet.ServletContext;

public class InitCommandInvoker {

	// 使用SPI回调机制初始化命令队列
	private ServiceLoader<InitCommand> loader = null;
	private ServletContext servletContext;

	public InitCommandInvoker( ServletContext servletContext) {
		this.servletContext = servletContext;
		// 使用ServiceLoader来加载SPI实现类
		// 对应的实现类名称必须放在META-INF\services目录下
		loader = ServiceLoader.load(InitCommand.class);
	}
	
	/**
	 * 执行初始化命令队列
	 */
	public void doExecute(){
		for (InitCommand command : loader) {
			command.execute(servletContext);
		}
	}

}
