package org.evergreen.beans.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 
 * @author wangl 命令执行Invoker
 */
public class DependencyInvoker {

	private static List<InjectHandler> handlers = new ArrayList<>();


	static {
		Iterator<InjectHandler> iterator = ServiceLoader.load(InjectHandler.class).iterator();
		while(iterator.hasNext()){
			handlers.add(iterator.next());
		}
	}

	// 执行注入操作
	public static Object inject(Object bean, Class<?> targetClass,BeanFactory factory) {
		for (InjectHandler handler : handlers) {
			handler.execute(bean, targetClass, factory);
		}
		return bean;
	}
}
