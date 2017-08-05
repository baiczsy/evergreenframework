package org.evergreen.web;

/**
 * Action映射处理器
 */
public interface HandlerMapping {

    ActionMapper handler() throws Exception;
}
