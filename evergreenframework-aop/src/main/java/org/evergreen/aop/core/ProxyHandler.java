package org.evergreen.aop.core;

public interface ProxyHandler {
    
	<T> T createProxy(Class<T> targetClass);

}
