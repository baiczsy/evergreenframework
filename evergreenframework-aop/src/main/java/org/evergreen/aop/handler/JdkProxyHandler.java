package org.evergreen.aop.handler;

import java.lang.reflect.Proxy;

import org.evergreen.aop.ProxyHandler;
import org.evergreen.aop.invocation.handler.JdkInvocationHandler;

/**
 * JDK动态代理构建处理器
 */
public class JdkProxyHandler implements ProxyHandler {

	/**
	 * 使用JDK动态代理
	 */
	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<T> targetClass) {
		try {
			Object targetInstance = targetClass.newInstance();
			JdkInvocationHandler handler = new JdkInvocationHandler(targetInstance);
			return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					targetClass.getInterfaces(), handler);
		} catch (Exception e) {
			throw new RuntimeException("Create JdkProxyHandler fail.", e);
		}
	}
}
