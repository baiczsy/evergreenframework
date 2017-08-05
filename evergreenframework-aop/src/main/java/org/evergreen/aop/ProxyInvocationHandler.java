package org.evergreen.aop;

import java.lang.reflect.Method;

import org.evergreen.aop.annotation.Exclude;

public abstract class ProxyInvocationHandler {

	/**
	 * 拦截器栈,用于存放通知
	 */
	protected InterceptorStack stack;

	/**
	 * 回调上下文
	 */
	protected InvocationContext invocationContext;

	/**
	 * 初始化回调上下文
	 * @param target
	 * @param method
	 * @param args
	 * @param invocationContext
     */
	protected void initInvocationContext(Object target, Method method, Object[] args,
						InvocationContextImpl invocationContext) {
		stack = new InterceptorStack(target, method);
		invocationContext.setStack(stack);
		invocationContext.setTarget(target);
		invocationContext.setMethod(method);
		invocationContext.setParameters(args);
		this.invocationContext = invocationContext;
	}

	// 回调拦截器栈，并返回结果
	protected Object invokeStack() throws Throwable {
		return invocationContext.proceed();
	}

	// 判断是否是Object中的方法
	private boolean isObjectMethod(Method method) {
		return (method.getDeclaringClass() == Object.class);
	}

	// 判断拦截器栈中是否存有拦截器
	private boolean hasInterceptors() {
		return (stack.getAdviceMethods().size() != 0) ? true : false;
	}

	// 排除标识为Exclude注解的方法
	private boolean isPresentInject(Method method) {
		return method.isAnnotationPresent(Exclude.class) ? true : false;
	}

	// 校验回调方法有效性
	protected boolean isEffectiveMethod(Method method) {
		return (!isPresentInject(method) && hasInterceptors() && !isObjectMethod(method)) ? true
				: false;
	}

}
