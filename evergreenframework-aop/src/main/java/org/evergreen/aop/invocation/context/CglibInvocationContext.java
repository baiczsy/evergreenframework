package org.evergreen.aop.invocation.context;

import org.evergreen.aop.InvocationContextImpl;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 回调上下文
 */
public class CglibInvocationContext extends InvocationContextImpl {

	private MethodProxy proxy;

	public CglibInvocationContext(Object target, Method method, Object[] parameters){
		super(target, method, parameters);
	}

	public void setProxy(MethodProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	protected Object invokeTarget() throws Throwable {
		// 调用目标对象的行为方法
		return proxy.invokeSuper(target, parameters);
	}

}
