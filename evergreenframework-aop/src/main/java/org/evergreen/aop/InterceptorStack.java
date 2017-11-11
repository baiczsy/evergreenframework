package org.evergreen.aop;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Stack;

import org.evergreen.aop.annotation.Around;
import org.evergreen.aop.annotation.Interceptors;

/**
 * 拦截器栈
 * 
 * @author wangl
 * 
 */
public class InterceptorStack extends Stack<Method>{

	private static ThreadLocal<InterceptorStack> local = new ThreadLocal<InterceptorStack>();

	public static InterceptorStack getInterceptorStack(){
		if(local.get() == null){
			local.set(new InterceptorStack());
		}
		return local.get();
	}

	private InterceptorStack(){
	}

	/**
	 * 将拦截器中带有通知注解的方法放入到拦截器栈中
	 * 
	 * @param interceptors
	 */
	void pushAdvice(Interceptors interceptors) {
		// 根据value的值取出注解中的参数数组，就是一组拦截器的class对象
		Class<?>[] inters = interceptors.value();
		// 迭代数组，获取每一个拦截器
		for (Class<?> interceptorClazz : inters) {
			for (Method method : interceptorClazz.getMethods()) {
				if (method.isAnnotationPresent(Around.class)){
					//将环绕通知方法存入Stack
					push(method);
				}
			}
		}
	}

	public void removeLocal(){
		local.remove();
	}
}
