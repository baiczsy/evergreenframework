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
    public static Stack<AdviceInfo> createAdviceStack(Method method){
        Stack<AdviceInfo> stack = new Stack<AdviceInfo>();
        if(method.getDeclaringClass().isAnnotationPresent(Interceptors.class)){
            resolve(method.getDeclaringClass().getAnnotation(Interceptors.class), stack);
        }
        if (method.isAnnotationPresent(Interceptors.class)) {
            resolve(method.getAnnotation(Interceptors.class), stack);
        }
        return stack;
    }

    /**
     *  解析Interceptors注解中的切面信息并将通知存入Stack中
     * @param annotation
     * @param stack
     */
    private static void resolve(Interceptors annotation, Stack<AdviceInfo> stack){
        // 根据value的值取出注解中的参数数组，就是一组拦截器的class对象
        Class<?>[] inters = annotation.value();
        // 迭代数组，获取每一个拦截器
        for (Class<?> aspectClass : inters) {
            //创建切面实例
            Object aspectInstance = newAspectInstance(aspectClass);
            for (Method advice : aspectClass.getMethods()) {
                if (advice.isAnnotationPresent(Around.class)){
                    AdviceInfo adviceInfo = createAdviceInfo(aspectInstance, advice);
                    //将通知描述存入Stack
                    stack.push(adviceInfo);
                }
            }
        }
    }

    /**
     * 创建通知描述信息
     * @param aspectInstance
     * @param advice
     * @return
     */
    private static AdviceInfo createAdviceInfo(Object aspectInstance, Method advice){
        AdviceInfo adviceInfo = new AdviceInfo();
        adviceInfo.setAspectInstance(aspectInstance);
        adviceInfo.setAdvice(advice);
        return adviceInfo;
    }

    /**
     * 创建切面实例
     * @param aspectClass
     * @return
     */
    private static Object newAspectInstance(Class<?> aspectClass){
        try {
            return aspectClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Create aspect instance fail.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Create aspect instance fail.", e);
        }
    }
}
