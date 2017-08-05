package org.evergreen.aop.invocation.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.evergreen.aop.ProxyInvocationHandler;
import org.evergreen.aop.invocation.context.JdkInvocationContext;

/*
 * JDK回调处理器
 */
public class JdkInvacationHandler extends ProxyInvocationHandler implements
		InvocationHandler {

	private Object target;

	public JdkInvacationHandler(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		// 参数method获取的是接口中的方法，并没有注解，因此必须获取该接口相应实现类的方法
		// 实现类的方法中定义了注解
		Method targetMethod = target.getClass().getMethod(method.getName(),
				method.getParameterTypes());
		//初始化回调上下文
		initInvocationContext(target, targetMethod, args, new JdkInvocationContext());
		// 校验方法有效性
		// 如果当前方法不是注解注入方法,
		// 并且当前的方法不是继承自Object,
		// 并且拦截器栈中存有拦截器，则委托给外部回调处理器
		if (isEffectiveMethod(method))
			// 调用拦截器栈，并返回结果
			return invokeStack();
		// 否则直接回调目标方法
		return method.invoke(target, args);
	}
}
