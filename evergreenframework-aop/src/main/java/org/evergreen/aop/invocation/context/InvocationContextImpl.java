package org.evergreen.aop.invocation.context;

import org.evergreen.aop.InvocationContext;
import org.evergreen.aop.annotation.Exclude;
import org.evergreen.aop.annotation.Interceptors;

import java.lang.reflect.Method;

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
	private InterceptorStack stack = InterceptorStack.getInterceptorStack();

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

	public InvocationContextImpl(Object target, Method method, Object[] parameters){
		this.target = target;
		this.method = method;
		this.parameters = parameters;
		resolveAnnotation();
	}

	/**
	 * 解析方法上以及定义该方法的类上定义了Interceptors注解,
	 * 并将注解定义的拦截器添加到拦截器栈
	 */
	private void resolveAnnotation(){
		if (method.isAnnotationPresent(Interceptors.class)) {
			stack.pushAdvice(method.getAnnotation(Interceptors.class));
		}
		if(method.getDeclaringClass().isAnnotationPresent(Interceptors.class)){
			stack.pushAdvice(method.getDeclaringClass().getAnnotation(Interceptors.class));
		}

	}

	/**
	 * 调用拦截器栈
	 * 
	 * @throws Throwable
	 */
	public Object proceed() throws Throwable {
		// 排除继承自Object中的方法和标识为Exclude注解的方法,这些方法不参与代理
		if(!method.getDeclaringClass().equals(Object.class) && !method.isAnnotationPresent(Exclude.class)){
			// 如果环绕通知栈不为空，则执行栈中的通知方法
			if (!stack.empty()) {
				Method advice = stack.pop();
				return advice.invoke(advice.getDeclaringClass().newInstance(), this);
			}
		}
		// 调用目标对象的方法
		Object obj = invokeTarget();
		stack.removeLocal();
		return obj;
	}

	/**
	 * 回调处理,调用目标对象的具体行为
	 */
	protected abstract Object invokeTarget() throws Throwable;
}
