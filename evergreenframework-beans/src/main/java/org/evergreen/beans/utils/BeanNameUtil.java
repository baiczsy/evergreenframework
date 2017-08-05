package org.evergreen.beans.utils;
/**
 * 工具类,修改类名
 * @author lenovo
 *
 */
public class BeanNameUtil {

	public static String toLowerBeanName(String beanName) {
		String[] cname = beanName.split("\\.");
		beanName = cname[cname.length - 1];
		beanName = beanName.substring(0, 1).toLowerCase()
				+ beanName.substring(1);
		return beanName;
	}
}
