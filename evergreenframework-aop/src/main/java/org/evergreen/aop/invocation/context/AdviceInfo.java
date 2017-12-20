package org.evergreen.aop.invocation.context;

import java.lang.reflect.Method;

/**
 * 通知描述，包含通知以及切面实例
 */
class AdviceInfo {

    /**
     * 通知
     */
    private Method advice;

    /**
     * 切面实例
     */
    private Object aspectInstance;

    public Method getAdvice() {
        return advice;
    }

    public void setAdvice(Method advice) {
        this.advice = advice;
    }

    public Object getAspectInstance() {
        return aspectInstance;
    }

    public void setAspectInstance(Object aspectInstance) {
        this.aspectInstance = aspectInstance;
    }
}
