package org.evergreen.aop.invocation.handler;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.evergreen.aop.invocation.context.CglibInvocationContext;

/**
 * CGLIB回调处理器
 */
public class CglibInvocationHandler implements MethodInterceptor {

	public Object intercept(Object target, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		//创建CGLIB回调上下文
		CglibInvocationContext invocationContext = new CglibInvocationContext(target, method, args);
		// CGLIB回调上下文中需要设置methodProxy
		invocationContext.setProxy(proxy);
		// 调用拦截器栈，并返回结果
		return invocationContext.proceed();
	}
}
