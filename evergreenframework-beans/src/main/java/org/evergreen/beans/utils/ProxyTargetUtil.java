package org.evergreen.beans.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyTargetUtil {

    public static Object getTarget(Object bean) throws NoSuchFieldException, IllegalAccessException {
        if (Proxy.isProxyClass(bean.getClass())) {
            InvocationHandler invocationHandler = Proxy
                    .getInvocationHandler(bean);
            Field field = invocationHandler.getClass().getDeclaredField(
                    "target");
            field.setAccessible(true);
            bean = field.get(invocationHandler);
            return bean;
        }
        return bean;
    }
}
