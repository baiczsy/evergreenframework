package org.evergreen.web.factory;

import java.io.IOException;

import org.evergreen.web.ActionDefinition;
import org.evergreen.web.ActionFactory;
import org.evergreen.web.exception.ActionException;
import org.evergreen.web.exception.RequestMappingException;

public class WebApplicationFactory implements ActionFactory {

	/**
	 * 从ServletContext中初始化action方法
	 * 
	 * @param definition
	 * @return
	 * @throws ActionException
	 * @throws IOException
	 */
	public Object crateAction(ActionDefinition definition)
			throws IOException, ActionException {
		if (definition != null) {
			try {
				return definition.getActionClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RequestMappingException();
		}
		return null;
	}

}
