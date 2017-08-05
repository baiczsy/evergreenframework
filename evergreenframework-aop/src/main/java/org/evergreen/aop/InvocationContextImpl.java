package org.evergreen.aop;

import java.lang.reflect.Method;
import java.util.LinkedList;

public abstract class InvocationContextImpl implements InvocationContext {

	/**
	 * 目标对象
	 */
	protected Object target;
	/**
	 * 目标方法
	 */
	protected Method method;
	/**
	 * 目标方法参数
	 */
	protected Object[] parameters;
	/**
	 * 环绕通知栈
	 */
	protected LinkedList<Method> adviceMethods;
	

	/**
	 * 获取目标对象的所有参数
	 */
	public Object[] getParameters() {
		return parameters;
	}
	
	protected void setParameters(Object[] parameters){
		this.parameters = parameters;
	}

	/**
	 * 获取目标对象
	 */
	public Object getTarget() {
		return target;
	}
	
	protected void setTarget(Object target){
		this.target = target;
	}

	/**
	 * 获取当前调用的方法
	 */
	public Method getMethod() {
		return method;
	}
	
	protected void setMethod(Method method){
		this.method = method;
	}
	
	protected void setStack(InterceptorStack stack){
		this.adviceMethods = stack.getAdviceMethods();
	}

	/**
	 * 环绕通知回调
	 */
	protected Object invokeAroundAdvice() throws Exception {
		// 从栈中尾部取出一个通知执行
		Method method = adviceMethods.removeFirst();
		return method.invoke(method.getDeclaringClass().newInstance(), this);

	}

	/**
	 * 调用拦截器栈
	 * 
	 * @throws Throwable
	 */
	public Object proceed() throws Throwable {
		// 如果环绕通知栈不为空，那么继续执行栈中的通知方法
		if (!adviceMethods.isEmpty()) {
			return invokeAroundAdvice();
		}
		// 拦截器栈调用完后将调用目标对象的方法
		return invokeProcess();
	}

	/**
	 * 回调处理,调用目标对象的具体行为,不同的代理有不同的调用机制
	 */
	protected abstract Object invokeProcess() throws Throwable;
}
