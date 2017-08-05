package org.evergreen.web.initial;

import java.lang.reflect.Modifier;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

@HandlesTypes(WebAppInitializer.class)
public class EvergreenContaninerInitializer implements
		ServletContainerInitializer {

	// classes集合封装了@HandlesTypes注解定义的接口的所有实现类
	public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
			throws ServletException {
		if (classes != null) {
			for (Class<?> clazz : classes) {
				// 判断此class是否是一个接口
				if (!clazz.isInterface()
				// 根据clazz.getModifiers()返回的访问修饰符编码号,判定是否是一个抽象类
						&& !Modifier.isAbstract(clazz.getModifiers())
						// 判定此 WebAppInitializer接口与指定的
						// clazz对象所表示的类或接口是否相同，或是否是其超类或超接口
						&& WebAppInitializer.class.isAssignableFrom(clazz)) {
					try {
						((WebAppInitializer) clazz.newInstance())
								.onStartup(servletContext);
					} catch (Throwable ex) {
						throw new ServletException(
								"Failed to instantiate WebAppInitializer class",
								ex);
					}
				}
			}
		}
	}
}
