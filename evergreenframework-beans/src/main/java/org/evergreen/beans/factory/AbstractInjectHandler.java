package org.evergreen.beans.factory;

import org.evergreen.beans.factory.exception.NoSuchBeanException;

import java.util.Map;

import javax.annotation.Resource;

public abstract class AbstractInjectHandler implements InjectHandler {

	/**
	 * 
	 * @param factory
	 *            容器工厂
	 * @param annotation
	 *            Resource注解
	 * @param resourceType
	 *            注入属性的类型
	 * @param resourceName
	 *            字段的默认名称, 当没有指定name和type属性时,先根据字段的默认名称查找,如果未找到,再根据类型查找
	 * @return
	 */
	protected Object getBean(BeanFactory factory, Resource annotation,
			Class<?> resourceType, String resourceName) {
		// 按name属性查找
		if (!"".equals(annotation.name()) && annotation.type().equals(Object.class)) {
			return getByName(factory, annotation.name());
		}
		// 按type属性查找
		if ("".equals(annotation.name()) && !annotation.type().equals(Object.class)) {
			return getByType(factory, annotation.type(), resourceType);
		}
		// 按name和type查找
		if (!"".equals(annotation.name()) && !annotation.type().equals(Object.class)) {
			return getByNameType(factory, annotation.name(), annotation.type(),
					resourceType);
			// 默认匹配查找
		} else {
			return getByDefault(factory, resourceType, resourceName);
		}
	}

	/**
	 * 根据name查找
	 * 
	 * @param factory
	 * @param name
	 * @return
	 * @throws NoSuchBeanException
	 */
	private Object getByName(BeanFactory factory, String name)
			throws NoSuchBeanException {
		if (factory.getDefinitionMap().containsKey(name)) {
			return factory.getBean(name);
		}
		throw new NoSuchBeanException("No bean named '" + name
				+ "' is defined");
	}

	/**
	 * 根据type查找
	 * 
	 * @param factory
	 * @param annotationType
	 * @return
	 */
	private Object getByType(BeanFactory factory, Class<?> annotationType,
			Class<?> resourceType) throws ClassCastException {
		Map<String, BeanDefinition> definitionMap = factory.getDefinitionMap();
		for (String key : definitionMap.keySet()) {
			if (annotationType == definitionMap.get(key).getBeanClass())
				return factory.getBean(key);
		}
		throw new ClassCastException(annotationType.getName()
				+ " cannot be cast to " + resourceType.getName());
	}

	/**
	 * 根据名称和类型查找
	 * 
	 * @param factory
	 * @param name
	 * @param annotationType
	 * @return
	 */
	private Object getByNameType(BeanFactory factory, String name,
			Class<?> annotationType, Class<?> resourceType) {
		Map<String, BeanDefinition> definitionMap = factory.getDefinitionMap();
		if (definitionMap.containsKey(name)) {
			if (annotationType == definitionMap.get(name).getBeanClass())
				return factory.getBean(name);
			throw new ClassCastException(annotationType.getName()
					+ " cannot be cast to " + resourceType.getName());
		}
		throw new NoSuchBeanException("No bean named '" + name
				+ "' is defined");
	}

	/**
	 * 默认查找方式
	 * 
	 * @param factory
	 * @param resourceType
	 * @return
	 */
	private Object getByDefault(BeanFactory factory, Class<?> resourceType,
			String resourceName) {
		Map<String, BeanDefinition> definitionMap = factory.getDefinitionMap();
		// 先根据字段名查找,如果在容器中检索到,直接返回
		if (definitionMap.containsKey(resourceName)) {
			return factory.getBean(resourceName);
		}
		// 在容器中检索不到,则按类型查找
		for (String key : definitionMap.keySet()) {
			if (resourceType.isAssignableFrom(definitionMap.get(key)
					.getBeanClass()))
				return factory.getBean(key);
		}
		throw new NoSuchBeanException(
				"Can not find the components,injection of resource dependencies failed");
	}

}
