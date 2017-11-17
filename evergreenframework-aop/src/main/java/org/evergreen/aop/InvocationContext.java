package org.evergreen.aop;

import java.lang.reflect.Method;

public interface InvocationContext {

	/**
     * 获取目标对象的所有参数
     */
	Object[] getParameters();
	
	/**
	 * 获取目标对象
	 */
	Object getTarget();
	
	/**
	 * 获取当前调用的方法
	 */
	Method getMethod();
	
	/**
	 * 回调处理
	 * @throws Throwable
	 */
	Object proceed() throws Throwable;
}
