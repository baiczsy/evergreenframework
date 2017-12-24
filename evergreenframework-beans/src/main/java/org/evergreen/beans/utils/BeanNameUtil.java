package org.evergreen.beans.utils;

import org.evergreen.beans.annotation.Component;

/**
 * 获取bean在容器中的标识名称
 * beanName作为容器的key,当Component没有指定名字的时候,默认使用当前的类名作为bean的名字 并将类名的首字母转换成小写
 * @author lenovo
 *
 */
public class BeanNameUtil {

	public static String getBeanName(Class<?> beanClass){
		Component annotation = beanClass.getAnnotation(Component.class);
		return ("".equals(annotation.value())) ? toLowerBeanName(beanClass.getSimpleName()) : annotation
				.value();
	}

	private static String toLowerBeanName(String beanName) {
		String[] cname = beanName.split("\\.");
		beanName = cname[cname.length - 1];
		beanName = beanName.substring(0, 1).toLowerCase()
				+ beanName.substring(1);
		return beanName;
	}
}
