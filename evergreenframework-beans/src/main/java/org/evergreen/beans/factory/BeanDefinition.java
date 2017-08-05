package org.evergreen.beans.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean的表述定义,在重启初始化时,将扫描所有带@Com注解的类 然后为每一个类创建一个BeanDefinition的描述定义
 * 
 * @author lenovo
 *
 */
public class BeanDefinition {

	/**
	 * bean的作用域(创建方式)
	 */
	private String scope;

	/**
	 * bean的Class
	 */
	private Class<?> beanClass;

	/**
	 * bean的初始化方法
	 */
	private List<Method> initMethos = new ArrayList<Method>();

	/**
	 * bean的销毁方法
	 */
	private List<Method> destroyMethos = new ArrayList<Method>();

	/**
	 * 是否创建代理
	 * 
	 */
	private boolean proxy;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public List<Method> getInitMethos() {
		return initMethos;
	}

	public List<Method> getDestroyMethos() {
		return destroyMethos;
	}

	public boolean isProxy() {
		return proxy;
	}

	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}

}
