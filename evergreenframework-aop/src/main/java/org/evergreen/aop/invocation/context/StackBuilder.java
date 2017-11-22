package org.evergreen.aop.invocation.context;

import org.evergreen.aop.annotation.Around;
import org.evergreen.aop.annotation.Interceptors;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * 拦截器栈构建器
 */
class StackBuilder {

    /**
     * 解析方法上以及定义该方法的类上的Interceptors注解,
     */
    public static Stack<Method> createAdviceStack(Method method){
        Stack<Method> stack = new Stack<Method>();
        if (method.isAnnotationPresent(Interceptors.class)) {
            pushStack(method.getAnnotation(Interceptors.class), stack);
        }
        if(method.getDeclaringClass().isAnnotationPresent(Interceptors.class)){
            pushStack(method.getDeclaringClass().getAnnotation(Interceptors.class), stack);
        }
        return stack;
    }

    /**
     *  将Interceptors注解标识的切面加入Stack中
     * @param annotation
     * @param stack
     */
    private static void pushStack(Interceptors annotation, Stack<Method> stack){
        // 根据value的值取出注解中的参数数组，就是一组拦截器的class对象
        Class<?>[] inters = annotation.value();
        // 迭代数组，获取每一个拦截器
        for (Class<?> interceptorClazz : inters) {
            for (Method method : interceptorClazz.getMethods()) {
                if (method.isAnnotationPresent(Around.class)){
                    //将环绕通知方法存入Stack
                    stack.push(method);
                }
            }
        }
    }
}
