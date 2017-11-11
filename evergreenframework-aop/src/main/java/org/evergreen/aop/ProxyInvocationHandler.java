package org.evergreen.aop;

import java.lang.reflect.Method;

import org.evergreen.aop.annotation.Exclude;

public abstract class ProxyInvocationHandler {

	/**
	 * 回调上下文
	 */
	protected InvocationContext invocationContext;

	/**
	 * 设置回调上下文
	 * @param args
	 * @param invocationContext
     */
	protected void setInvocationContext(InvocationContext invocationContext) {
		this.invocationContext = invocationContext;
	}

	// 判断是否是Object中的方法
	private boolean isObjectMethod(Method method) {
		return (method.getDeclaringClass().equals(Object.class));
	}

	// 排除标识为Exclude注解的方法
	private boolean isPresentInject(Method method) {
		return method.isAnnotationPresent(Exclude.class) ? true : false;
	}

	// 校验回调方法有效性
	protected boolean isEffectiveMethod(Method method) {
		return (!isPresentInject(method) && !isObjectMethod(method)) ? true
				: false;
	}

	// 回调拦截器栈，并返回结果
	protected Object invokeStack() throws Throwable {
		return invocationContext.proceed();
	}

}
