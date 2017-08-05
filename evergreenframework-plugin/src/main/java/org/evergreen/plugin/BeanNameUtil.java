package org.evergreen.plugin;

public class BeanNameUtil {

	private final static String PATH_SPLIT = "\\.";

	public static String toLowerBeanName(String beanName) {
		String[] cname = beanName.split(PATH_SPLIT);
		beanName = cname[cname.length - 1];
		beanName = beanName.substring(0, 1).toLowerCase()
				+ beanName.substring(1);
		return beanName;
	}
}
