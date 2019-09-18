package org.evergreen.aop.core.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.evergreen.aop.core.ProxyHandler;
import org.evergreen.aop.core.context.JdkInvocationContext;

/**
 * JDK动态代理构建处理器
 */
public class JdkProxyHandler implements ProxyHandler {

	/**
	 * 使用JDK动态代理
	 */
	@Override
	public <T> T createProxy(Class<T> targetClass) {
		try {
			Object target = targetClass.newInstance();
			//JdkInvocationHandler handler = new JdkInvocationHandler(targetInstance);
			InvocationHandler handler = (proxy, method, args) -> {
				// 参数method获取的是接口中的方法，并没有注解，因此必须获取该接口相应实现类的方法
				// 实现类的方法中定义了注解
				Method targetMethod = target.getClass().getMethod(method.getName(),
						method.getParameterTypes());
				// 创建JDK回调上下文,执行回调处理
				JdkInvocationContext invocationContext = new JdkInvocationContext(target, targetMethod, args);
				return invocationContext.invoke();
			};
			return (T)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
					targetClass.getInterfaces(), handler);
		} catch (Exception e) {
			throw new RuntimeException("Create JdkProxyHandler fail.", e);
		}
	}
}
