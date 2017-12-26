package org.evergreen.web;

import org.evergreen.web.exception.ActionException;

import javax.servlet.http.HttpServletRequest;

/**
 * Action映射处理器
 */
public interface HandlerMapping {

    ActionMapper handler(HttpServletRequest request) throws ActionException;
}
