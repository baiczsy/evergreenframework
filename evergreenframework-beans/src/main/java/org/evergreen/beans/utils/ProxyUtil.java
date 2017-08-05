package org.evergreen.beans.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * Created by wangl on 2016/4/15.
 */
public class ProxyUtil {

    private final static String PROXY_BUILDER = "org.evergreen.aop.ProxyBuilder";
    private final static String PROXY_CONTEXT_METHOD = "createProxy";
    private final static String INTERCEPTOR_CLASS_NAME = "org.evergreen.aop.annotation.Interceptors";
    private static Class<? extends Annotation> proxyAnnotation;
    private static Class<?> proxyBuilder;

    /**
     * 加载ProxyBuilder类
     */
    static {
        try {
            proxyAnnotation = (Class<? extends Annotation>) Class.forName(INTERCEPTOR_CLASS_NAME);
            proxyBuilder = Class.forName(PROXY_BUILDER);
        } catch (Exception e) {
        }
    }

    /**
     * 通过反射创建ProxyBuilder对象，
     * 通过ProxyBuilder对象来创建代理实例
     *
     * @param beanClass
     * @return
     */
    public static Object createProxy(Class<?> beanClass) {
        try {
            // 获取代理处理器上下文的构造方法
            Constructor<?> constructor = proxyBuilder.getConstructor(Class.class);
            // 创建代理处理器实例
            Object instance = constructor.newInstance(beanClass);
            // 回调代理处理器的createProxy方法创建代理对象
            return proxyBuilder.getMethod(PROXY_CONTEXT_METHOD)
                    .invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否已经加载了拦截器注解
     * @return
     */
    public static boolean hasProxyAnnotation(){
        return proxyAnnotation == null ? false : true;
    }

    public static Class<? extends Annotation> getProxyAnnotation(){
        return proxyAnnotation;
    }
}
