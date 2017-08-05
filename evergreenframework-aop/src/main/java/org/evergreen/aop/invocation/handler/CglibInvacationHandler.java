package org.evergreen.aop.invocation.handler;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.evergreen.aop.ProxyInvocationHandler;
import org.evergreen.aop.invocation.context.CglibInvocationContext;

/*
 * CGLIB回调处理器
 */
public class CglibInvacationHandler extends ProxyInvocationHandler implements
		MethodInterceptor {

	public Object intercept(Object target, Method method, Object[] args,
			MethodProxy proxy) throws Throwable {
		//初始化回调上下文
		initInvocationContext(target, method, args, new CglibInvocationContext());
		// 校验方法有效性
		// 如果当前方法不是@Exculte注解标注的方法,
		// 并且当前的方法不是继承自Object(也就是不需要代理Object类中的方法),
		// 并且拦截器栈中存还有其他的通知方法，
		// 则委托给外部回调处理器
		if (isEffectiveMethod(method)) {
			// CGLIB回调上下文中需要额外注入一个methodProxy
			((CglibInvocationContext) invocationContext).setProxy(proxy);
			// 调用拦截器栈，并返回结果
			return invokeStack();
		}
		// 否则直接回调目标方法
		return proxy.invokeSuper(target, args);
	}
}
