package org.evergreen.aop.invocation.context;

import org.evergreen.aop.InvocationContextImpl;

import net.sf.cglib.proxy.MethodProxy;

/*
 * CGLIB 回调上下文
 */
public class CglibInvocationContext extends InvocationContextImpl {

	private MethodProxy proxy;

	public void setProxy(MethodProxy proxy) {
		this.proxy = proxy;
	}

	protected Object invokeProcess() throws Throwable {
		// 调用目标对象的行为方法
		return proxy.invokeSuper(target, parameters);
	}

}
