package org.evergreen.beans.factory;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 
 * @author wangl 命令执行Invoker
 */
public class DependencyInvoker {

	/**
	 * 使用ServiceLoader(SPI方式)初始化注入器
	 */
	private static ServiceLoader<InjectHandler> loaders;

	static {
		loaders = ServiceLoader.load(InjectHandler.class);
	}

	// 执行注入操作
	public static Object inject(Object bean, Class<?> targetClass,BeanFactory factory) {
		Iterator<InjectHandler> iterator = loaders.iterator();
		// 遍历ServiceLoader,取出每一个注入器执行
		while (iterator.hasNext()) {
			iterator.next().execute(bean, targetClass, factory);
		}
		return bean;
	}
}
