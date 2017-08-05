package org.evergreen.beans.factory;

import java.util.ServiceLoader;

/**
 * 
 * @author wangl 命令执行Invoker
 */
public class DependencyInvoker {

	/**
	 * 使用ServiceLoader(SPI方式)初始化注入器
	 */
	private ServiceLoader<InjectHandler> loaders;
	/**
	 * Bean容器
	 */
	private BeanFactory factory;

	/**
	 * 目标Class对象
	 */
	private Class<?> targetClass;

	/**
	 * 目标对象
	 */
	private Object target;

	public DependencyInvoker(Object bean, Class<?> targetClass,
			BeanFactory factory) {
		this.target = bean;
		this.targetClass = targetClass;
		this.factory = factory;
		// 使用ServiceLoader来加载SPI实现类
		// 对应的实现类的完整类名必须放在META-INF\services目录下的org.ioc.core.InjectComand文件中
		// 并且该文件名必须是接口的完整类名
		// 调用ServiceLoader的load方法加载此文件,并自动创建好文件中定义的所有实现类的实例,
		// 保存在ServiceLoader当中,所以ServiceLoader也相当于一个集合
		loaders = ServiceLoader.load(InjectHandler.class);
	}

	// 执行注入操作
	public Object inject() {
		// 遍历ServiceLoader,取出每一个注入器执行
		for (InjectHandler command : loaders)
			// 执行命令
			command.execute(target, targetClass, factory);
		return target;
	}
}
