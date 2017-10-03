package org.evergreen.web;

import java.io.IOException;

import org.evergreen.web.exception.ActionException;

/**
 * 这个回调处理器用于从不同的环境中获取Action实例,如Spring容器,Evergreen容器
 * @author ThinkPad
 *
 */
public interface ActionFactory {

	Object crateAction(ActionDefinition definition)
			throws IOException, ActionException;
}
