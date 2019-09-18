package org.evergreen.aop.core.context;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 回调上下文
 */
public class CglibInvocationContext extends InvocationContextImpl {

	private MethodProxy methodProxy;
	private Object proxy;

	public CglibInvocationContext(Object target, Method method, Object[] parameters){
		super(target, method, parameters);
	}

	public void setProxy(Object proxy) {
		this.proxy = proxy;
	}

	public void setMethodProxy(MethodProxy methodProxy){
		this.methodProxy = methodProxy;
	}

	@Override
	protected Object invokeTarget() throws Throwable {
		// 调用目标对象的行为方法
		return methodProxy.invokeSuper(proxy, parameters);
	}

}
