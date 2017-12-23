package org.evergreen.web;

import java.io.IOException;

import org.evergreen.web.exception.ActionException;

/**
 * Action工厂，用于构建Action实例
 * @author ThinkPad
 *
 */
public interface HandlerFactory {

	Object crateAction(ActionDefinition definition)
			throws IOException, ActionException;
}
