package org.evergreen.aop.invocation.handler;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.evergreen.aop.annotation.Exclude;
import org.evergreen.aop.invocation.context.CglibInvocationContext;

/**
 * CGLIB回调处理器
 */
public class CglibInvocationHandler implements MethodInterceptor {

	private Object target;

	public CglibInvocationHandler(Object target){
		this.target = target;
	}

	public Object intercept(Object proxy, Method method, Object[] args,
			MethodProxy methodProxy) throws Throwable {
		//创建CGLIB回调上下文
		CglibInvocationContext invocationContext = new CglibInvocationContext(target, method, args);
		// 设置methodProxy以及proxy对象
		invocationContext.setProxy(proxy);
		invocationContext.setMethodProxy(methodProxy);
		// 执行回调
		return invocationContext.invoke();
	}
}
