package org.evergreen.beans.factory;

/**
 * 
 * @author wangl 抽象注入命令
 */
public interface InjectHandler {

	// 抽象注入行为,便于不同的注入实现,例如属性注入或方法注入
	public void execute(Object target, Class<?> targetClass, BeanFactory factory);
}
