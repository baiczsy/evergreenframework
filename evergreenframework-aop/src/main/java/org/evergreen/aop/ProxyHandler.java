package org.evergreen.aop;

public interface ProxyHandler {
    
	public <T> T createProxy(Class<T> beanClass);

}
