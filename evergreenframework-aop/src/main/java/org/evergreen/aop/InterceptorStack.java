package org.evergreen.aop;

import java.lang.reflect.Method;
import java.util.LinkedList;

import org.evergreen.aop.annotation.Around;
import org.evergreen.aop.annotation.Interceptors;

/**
 * 拦截器栈
 * 
 * @author wangl
 * 
 */
public class InterceptorStack {

	// 环绕通知栈
	private LinkedList<Method> adviceMethods = new LinkedList<Method>();

	public InterceptorStack(Object target, Method method){
		// 如果类上定义了Interceptors注解,则将拦截器添加到拦截器栈
		if (target.getClass().isAnnotationPresent(Interceptors.class))
			addAdviceToStack(target.getClass()
					.getAnnotation(Interceptors.class));
		// 如果方法上定义了Interceptors注解,则将拦截器添加到拦截器栈
		if (method.isAnnotationPresent(Interceptors.class))
			addAdviceToStack(method.getAnnotation(Interceptors.class));
	}

	/**
	 * 将拦截器中带有通知注解的方法放入到拦截器栈中
	 * 
	 * @param interceptors
	 */
	private void addAdviceToStack(Interceptors interceptors) {
		// 根据value的值取出注解中的参数数组，就是一组拦截器的class对象
		Class<?>[] inters = interceptors.value();
		// 迭代数组，获取每一个拦截器
		for (Class<?> interceptorClazz : inters) {
			for (Method method : interceptorClazz.getMethods()) {
				if (method.isAnnotationPresent(Around.class)){
					//将环绕通知方法存入集合
					adviceMethods.add(method);
				}
			}
		}
	}

	public LinkedList<Method> getAdviceMethods() {
		return adviceMethods;
	}
}
