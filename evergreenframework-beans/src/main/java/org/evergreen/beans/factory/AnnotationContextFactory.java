package org.evergreen.beans.factory;

import org.evergreen.beans.annotation.ScopeType;

public class AnnotationContextFactory extends BeanFactory {

	/**
	 * 让父类根据包路径执行扫描初始化的工作
	 * 并初始化所有单例的Bean
	 * @param path
	 */
	public AnnotationContextFactory(String path) {
		super(path);
		initSingleton();
	}

	/**
	 * 初始化SINGLETON实例放入bean容器中 立即加载的方式会先初始化所有单例的Bean 并注册到容器中
	 */
	private void initSingleton() {
		for (String beanName : definitionMap.keySet()) {
			BeanDefinition definition = definitionMap.get(beanName);
			if (ScopeType.SINGLETON.equals(definition.getScope())) {
				registerBeanDefinition(beanName, definition);
			}
		}
	}

	/**
	 * 从容器中获取Bean的BeanDefinition
	 * 如果Bean的BeanDefinition的scope为singleton,则从singletonMap中获取单例
	 * 否则以原型的方式创建并返回
	 */
	protected Object doGetBean(String beanName) {
		BeanDefinition definition = getBeanDefinition(beanName);
		if(ScopeType.SINGLETON.equals(definition.getScope())
				&& beansMap.containsKey(beanName)){
			return beansMap.get(beanName);
		}
		return createBean(definition);
	}

}
