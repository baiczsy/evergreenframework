package org.evergreen.aop.invocation.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.evergreen.aop.invocation.context.JdkInvocationContext;

/**
 * JDK回调处理器
 */
public class JdkInvocationHandler implements InvocationHandler {

	private Object target;

	public JdkInvocationHandler(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// 参数method获取的是接口中的方法，并没有注解，因此必须获取该接口相应实现类的方法
		// 实现类的方法中定义了注解
		Method targetMethod = target.getClass().getMethod(method.getName(),
				method.getParameterTypes());
		// 创建JDK回调上下文
		JdkInvocationContext invocationContext = new JdkInvocationContext(target, targetMethod, args);
		// 调用拦截器栈，并返回结果
		return invocationContext.proceed();
	}
}
