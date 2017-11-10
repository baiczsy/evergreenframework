package org.evergreen.aop.handler;

import java.lang.reflect.Proxy;

import org.evergreen.aop.ProxyHandler;
import org.evergreen.aop.invocation.handler.JdkInvocationHandler;

public class JdkProxyHandler implements ProxyHandler {

	// 使用JDK动态代理
	@SuppressWarnings("unchecked")
	public <T> T createProxy(Class<T> beanClass) {
		try {
			Object beanInstance = beanClass.newInstance();
			JdkInvocationHandler handler = new JdkInvocationHandler(beanInstance);
			return (T)Proxy.newProxyInstance(beanInstance.getClass().getClassLoader(),
					beanInstance.getClass().getInterfaces(), handler);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
