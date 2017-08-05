package org.evergreen.beans.factory;

import org.evergreen.beans.annotation.ScopeType;

public class AnnotationBeanFactory extends BeanFactory {

	/**
	 * 让父类根据扫描对象执行扫描初始化的工作
	 * @param path
	 */
	public AnnotationBeanFactory(String path) {
		super(path);
	}

	/**
	 * 获取实例(延迟加载)
	 */
	protected Object doGetBean(String beanName) {
		//获取Bean的描述定义
		BeanDefinition definition = definitionMap.get(beanName);
		//如果是描述中指定的作用域为单例,那么将创建一个Bean实例,然后再放入容器
		if (ScopeType.SINGLETON.equals(definition.getScope()))
			//如果容器中存在单例的Bean实例,直接从容器中获取,否则创建一个Bean实例放入容器中并返回
			if(beansMap.get(beanName) != null) {
				return beansMap.get(beanName);
			} else {
				synchronized (beansMap) {
					return buildToContainer(beanName, definition);
				}
			}
		//否则以原型的方式创建Bean实例
		return createBean(definition);
	}

}
