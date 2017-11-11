package org.evergreen.aop.invocation.context;

import org.evergreen.aop.InvocationContextImpl;

import java.lang.reflect.Method;


public class JdkInvocationContext extends InvocationContextImpl {

	public JdkInvocationContext(Object target, Method method, Object[] parameters){
		super(target, method, parameters);
	}


	protected Object invokeProcess() throws Exception {
		return method.invoke(target, parameters);
	}

}
