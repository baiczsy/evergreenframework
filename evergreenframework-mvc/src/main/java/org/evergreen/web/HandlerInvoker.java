package org.evergreen.web;

/**
 * Action回调处理器
 */
public interface HandlerInvoker {

    ViewResult invoke(ActionMapper mapper) throws Throwable;
}
