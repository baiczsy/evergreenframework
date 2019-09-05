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
		assemblySingletons();
	}

	/**
	 * 预初始化SINGLETON实例放入bean容器中
	 */
	private void initSingleton() {
		for (String beanName : definitionMap.keySet()) {
			BeanDefinition definition = definitionMap.get(beanName);
			if (ScopeType.SINGLETON.equals(definition.getScope())) {
				registerSingleton(beanName, definition);
			}
		}
	}

	/**
	 * 装配singleton实例
	 */
	private void assemblySingletons() {
		for (String beanName : beansMap.keySet()) {
			//执行注入
			injectProperty(definitionMap.get(beanName).getBeanClass(), beansMap.get(beanName));
		}
	}

	/**
	 * 从容器中获取Bean的BeanDefinition
	 * 如果Bean的BeanDefinition的scope为singleton,则从singletonMap中获取单例
	 * 否则以原型的方式创建并返回
	 */
	@Override
	protected Object doGetBean(String beanName) {
		BeanDefinition definition = getBeanDefinition(beanName);
		if(ScopeType.SINGLETON.equals(definition.getScope())){
			return beansMap.get(beanName);
		}
		return assemblyPrototype(definition);
	}

}
