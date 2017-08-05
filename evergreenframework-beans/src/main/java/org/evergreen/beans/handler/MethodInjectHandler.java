package org.evergreen.beans.handler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.evergreen.beans.factory.AbstractInjectHandler;
import org.evergreen.beans.factory.BeanFactory;

public class MethodInjectHandler extends AbstractInjectHandler {

	public void execute(Object target, Class<?> targetClass, BeanFactory factory) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(targetClass,
					Object.class);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				Method setMethod = propertyDescriptor.getWriteMethod();
				if (setMethod != null
						&& setMethod.isAnnotationPresent(Resource.class)) {
					// 验证set方法的合法性
					if (isSetMethod(propertyDescriptor.getName(), setMethod)) {
						// 获取该方法上的Inject注解
						Resource annotation = setMethod
								.getAnnotation(Resource.class);
						// 根据注解ref属性的值,从容器获取bean实例
						Object property = getBean(factory, annotation,
								setMethod.getParameterTypes()[0],
								propertyDescriptor.getName());
						// 最后将回调set方法将property注入
						setMethod.invoke(target, property);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 验证set方法合法性
	private boolean isSetMethod(String propertyName, Method setMethod)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		String firstChar = propertyName.substring(0, 1);
		String setMethodName = "set"
				+ propertyName.replaceFirst(firstChar,
						firstChar.toUpperCase());
		if (setMethod.getName().equals(setMethodName))
			return true;
		else
			throw new NoSuchMethodException(setMethodName);
	}

}
