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
		aopHandler = createProxyHandler();
	}

	/**
	 * 依据类型或方法上是否定义拦截器注解
	 * 判断是使用CGLIB或是JDK代理 如果此类实现的接口数大于0,使用JDK动态代理 否则使用CGLIB来创建代理
	 *
	 * @return
	 */
	private ProxyHandler createProxyHandler() {
		if(isInterceptorClass() || isInterceptorMethod()){
			return beanClass.getInterfaces().length > 0 ? new JdkProxyHandler() : new CglibProxyHandler();
		}
		return null;
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
			if (method.isAnnotationPresent(Interceptors.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 使用代理生成器来创建代理对象
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T createProxy() {
		return (aopHandler != null) ? (T)aopHandler.createProxy(beanClass) : null;
	}
}
