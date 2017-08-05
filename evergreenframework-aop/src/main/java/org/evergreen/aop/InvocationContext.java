package org.evergreen.aop;

import java.lang.reflect.Method;

public interface InvocationContext {

	/**
     * 获取目标对象的所有参数
     */
	public Object[] getParameters();
	
	/**
	 * 获取目标对象
	 */
	public Object getTarget();
	
	/**
	 * 获取当前调用的方法
	 */
	public Method getMethod();
	
	/**
	 * 回调处理,让不同的子类实现不同的回调处理方法
	 * @throws Exception 
	 */
	public Object proceed() throws Throwable;
}
