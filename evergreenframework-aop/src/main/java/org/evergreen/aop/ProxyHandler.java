package org.evergreen.aop;

public interface ProxyHandler {
    
	<T> T createProxy(Class<T> beanClass);

}
