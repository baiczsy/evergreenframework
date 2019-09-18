package org.evergreen.aop.core.handler;

import net.sf.cglib.proxy.MethodInterceptor;
import org.evergreen.aop.core.ProxyHandler;
import org.evergreen.aop.core.context.CglibInvocationContext;
import org.evergreen.aop.invocation.handler.CglibInvocationHandler;

import net.sf.cglib.proxy.Enhancer;

/**
 * Cglib动态代理构建处理器
 */
public class CglibProxyHandler implements ProxyHandler {

	/**
	 * 使用cglib动态代理
	 */
	@Override
	public <T> T createProxy(Class<T> targetClass) {
		try {
			MethodInterceptor interceptor = (proxy, method, args, methodProxy) -> {
				//创建CGLIB回调上下文
				CglibInvocationContext invocationContext = new CglibInvocationContext(targetClass.newInstance(), method, args);
				// 设置methodProxy以及proxy对象
				invocationContext.setProxy(proxy);
				invocationContext.setMethodProxy(methodProxy);
				// 执行回调
				return invocationContext.invoke();
			};
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(targetClass);
			enhancer.setCallback(interceptor);
			return (T)enhancer.create();
		} catch (Exception e) {
			throw new RuntimeException("Create CglibProxyHandler fail.", e);
		}
	}
}
