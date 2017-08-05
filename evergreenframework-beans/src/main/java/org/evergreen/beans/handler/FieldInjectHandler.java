package org.evergreen.beans.handler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import javax.annotation.Resource;

import org.evergreen.beans.factory.AbstractInjectHandler;
import org.evergreen.beans.factory.BeanFactory;

/**
 * 
 * @author wangl 属性注入实现
 */
public class FieldInjectHandler extends AbstractInjectHandler {

	public void execute(Object target, Class<?> targetClass, BeanFactory factory) {
		try {
			// 遍历当前类中所有的属性(包括私有的),便于解析是否带有注解
			for (Field field : targetClass.getDeclaredFields()) {
				// 判断属性中是否定义了Inject注解类型
				if (field.isAnnotationPresent(Resource.class)) {
					// 获取该属性上的Inject注解
					Resource annotation = field.getAnnotation(Resource.class);
					// 根据注解ref属性的值,从容器获取bean实例(递归调用)
					Object property = getBean(factory, annotation,
							field.getType(), field.getName());
					// 如果属性是私有的,先打开访问开关
					field.setAccessible(true);
					// 给当前的field属性赋值(注入)
					field.set(target, property);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
