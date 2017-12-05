package org.evergreen.web;

import org.evergreen.web.exception.ActionException;

/**
 * Action映射处理器
 */
public interface HandlerMapping {

    ActionMapper handler() throws ActionException;
}
