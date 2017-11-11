package org.evergreen.aop;

import org.evergreen.aop.annotation.Interceptors;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Stack;

/**
 * 回调上下文
 */
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
	protected InterceptorStack stack = InterceptorStack.getInterceptorStack();


	public InvocationContextImpl(Object target, Method method, Object[] parameters){
		this.target = target;
		this.method = method;
		this.parameters = parameters;
		resolveAnnotation(method);
	}

	/**
	 * 解析方法上以及定义该方法的类上定义了Interceptors注解,
	 * 并将注解定义的拦截器添加到拦截器栈
	 */
	private void resolveAnnotation(Method method){
		if (method.isAnnotationPresent(Interceptors.class)) {
			stack.pushAdvice(method.getAnnotation(Interceptors.class));
		}
		if(method.getDeclaringClass().isAnnotationPresent(Interceptors.class)){
			stack.pushAdvice(method.getDeclaringClass().getAnnotation(Interceptors.class));
		}

	}

	/**
	 * 获取目标对象的所有参数
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * 获取目标对象
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * 获取当前调用的方法
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * 调用拦截器栈
	 * 
	 * @throws Throwable
	 */
	public Object proceed() throws Throwable {
		// 如果环绕通知栈不为空，那么继续执行栈中的通知方法
		if (!stack.empty()) {
			return invokeAdvice();
		}
		// 调用目标对象的方法
		Object obj = invokeProcess();
		stack.removeLocal();
		return obj;
	}

	/**
	 * 环绕通知回调
	 */
	protected Object invokeAdvice() throws Exception {
		// 从栈中尾部取出一个通知执行
		Method method = stack.pop();
		return method.invoke(method.getDeclaringClass().newInstance(), this);

	}

	/**
	 * 回调处理,调用目标对象的具体行为
	 */
	protected abstract Object invokeProcess() throws Throwable;
}
