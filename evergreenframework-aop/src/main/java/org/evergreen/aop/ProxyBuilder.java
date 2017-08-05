package org.evergreen.aop;

import java.lang.reflect.Method;

import org.evergreen.aop.annotation.Interceptors;
import org.evergreen.aop.handler.CglibProxyHandler;
import org.evergreen.aop.handler.JdkProxyHandler;

/**
 * 代理生成上下文
 * 
 * @author lenovo
 *
 */
public class ProxyBuilder {

	/**
	 * 目标对象
	 */
	private Class<?> beanClass;

	/**
	 * 代理类型的实例(JDK代理或者是CGLIB代理)
	 */
	private ProxyHandler aopHandler;

	public ProxyBuilder(Class<?> beanClass) {
		this.beanClass = beanClass;
		// 根据解析器解析目标对象后,获取需要创建代理的生成器类名
		aopHandler = resolveProxyHandler();
	}

	/**
	 * 判断是否需要创建代理,依据类型或方法上是否定义拦截器注解
	 * 如果需要,则获取代理生成器的完整类型
	 *
	 * @return
	 */
	private ProxyHandler resolveProxyHandler() {
		return (isInterceptorClass() || isInterceptorMethod()) ? getProxyType(beanClass
				.getInterfaces()) : null;
	}

	/**
	 * 依据类检测是否需要创建代理
	 *
	 * @return
	 */
	private boolean isInterceptorClass() {
		return beanClass.isAnnotationPresent(Interceptors.class) ? true : false;
	}

	/**
	 * 依据方法检测是否需要代理
	 *
	 * @return
	 */
	private boolean isInterceptorMethod() {
		for (Method method : beanClass.getMethods()) {
			if (method.isAnnotationPresent(Interceptors.class))
				return true;
		}
		return false;
	}

	/**
	 * 判断是使用CGLIB或是JDK代理 如果此类实现的接口数大于0,使用JDK动态代理 否则使用CGLIB来创建代理
	 * 
	 * @param interfaces  当前Class实现的所有接口
	 * @return
	 */
	private ProxyHandler getProxyType(Class<?>[] interfaces) {
		// 如果此类实现的接口数大于0,使用JDK动态代理
		// 否则使用CGLIB来创建代理
		return interfaces.length > 0 ? new JdkProxyHandler() : new CglibProxyHandler();
	}

	/**
	 * 使用代理生成器来创建代理对象
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T createProxy() {
		if (aopHandler != null)
			return (T)aopHandler.createProxy(beanClass);
		return null;
	}
}
