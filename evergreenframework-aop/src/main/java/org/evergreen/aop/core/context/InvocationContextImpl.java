package org.evergreen.aop.core.context;

import org.evergreen.aop.core.InvocationContext;
import org.evergreen.aop.annotation.Exclude;

import java.lang.reflect.Method;
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
    private Stack<AdviceInfo> stack;

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

    public InvocationContextImpl(Object target, Method method, Object[] parameters) {
        this.target = target;
        this.method = method;
        this.parameters = parameters;
        initAdviceStack();
    }

    /**
     * 创建环绕通知栈
     */
    private void initAdviceStack() {
        stack = StackBuilder.createAdviceStack(method);
    }

    /**
     * 排除继承自Object中的方法和标识为Exclude注解的方法,这些方法不参与代理
     * 如果是需要参与代理的方法，则执行proceed()方法调用拦截器栈
     * 否则直接回调目标行为
     *
     * @return Object
     */
    public Object invoke() throws Throwable {
        if (!method.getDeclaringClass().equals(Object.class) && !method.isAnnotationPresent(Exclude.class)) {
            return proceed();
        }
        return invokeTarget();
    }

    /**
     * 调用拦截器栈
     *
     * @throws Throwable
     */
    public Object proceed() throws Throwable {
        // 如果环绕通知栈不为空，则执行栈中的通知方法
        if (!stack.empty()) {
            AdviceInfo adviceInfo = stack.pop();
            return adviceInfo.getAdvice().invoke(adviceInfo.getAspectInstance(), this);
        }
        // 拦截器栈执行完成后，则继续调用目标对象的方法
        return invokeTarget();
    }

    /**
     * 回调处理,调用目标对象的具体行为
     */
    protected abstract Object invokeTarget() throws Throwable;
}
