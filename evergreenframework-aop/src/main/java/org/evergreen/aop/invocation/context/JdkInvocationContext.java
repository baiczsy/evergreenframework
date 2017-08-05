package org.evergreen.aop.invocation.context;

import org.evergreen.aop.InvocationContextImpl;


public class JdkInvocationContext extends InvocationContextImpl {

	protected Object invokeProcess() throws Exception {
		return method.invoke(target, parameters);
	}

}
