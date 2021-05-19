package org.evergreen.web.factory;

import java.io.IOException;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.HandlerFactory;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.TargetActionException;

public class WebAppHandlerFactory implements HandlerFactory {

	/**
	 * 从ServletContext中初始化action方法
	 * 
	 * @param definition
	 * @return
	 * @throws ActionException
	 * @throws IOException
	 */
	@Override
	public Object crateAction(ActionDefinition definition) throws ActionException {
		try {
			return definition.getActionClass().newInstance();
		} catch (InstantiationException e) {
			throw new TargetActionException("Create target action handler fail.", e);
		} catch (IllegalAccessException e) {
			throw new TargetActionException("Create target action handler fail.", e);
		}
	}

}
