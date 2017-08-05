package org.evergreen.plugin.init;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.evergreen.beans.factory.BeanFactory;
import org.evergreen.plugin.InitCommand;
import org.evergreen.plugin.StartService;
import org.evergreen.plugin.factory.EvergreenContainerFactory;

/**
 * 执行定时服务组件
 * @author lenovo
 *
 */
public class StartServiceInitCommand implements InitCommand{
	
	@SuppressWarnings("unchecked")
	public void execute(ServletContext servletContext) {
		BeanFactory factory = (BeanFactory) servletContext
				.getAttribute(EvergreenContainerFactory.BEAN_FACTORY);
		// 通过工厂获取Bean的描述定义集合
		try {
			Field field = BeanFactory.class.getDeclaredField("beansMap");
			field.setAccessible(true);
			ConcurrentHashMap<String, Object> beansMap = (ConcurrentHashMap<String, Object>) field
					.get(factory);
			for (String key : beansMap.keySet()) {
				Object bean = beansMap.get(key);
				Class<?>[] interfaces = bean.getClass().getInterfaces();
				for (Class<?> inf : interfaces) {
					if (inf == StartService.class) {
						StartService timerService = (StartService)bean;
						timerService.execute(servletContext);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
