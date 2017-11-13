package org.evergreen.aop;

import java.lang.reflect.Method;

import org.evergreen.aop.annotation.Exclude;

public class ProxyInvocationHandler {

	// 排除继承自Object中的方法
	private boolean isObjectMethod(Method method) {
		return (method.getDeclaringClass().equals(Object.class));
	}

	// 排除标识为Exclude注解的方法
	private boolean isExcludedMethod(Method method) {
		return method.isAnnotationPresent(Exclude.class);
	}

	// 校验回调方法有效性
	protected boolean isEffectiveMethod(Method method) {
		return (!isExcludedMethod(method) && !isObjectMethod(method));
	}
}
