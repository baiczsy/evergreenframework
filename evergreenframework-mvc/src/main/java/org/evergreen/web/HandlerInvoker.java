package org.evergreen.web;

/**
 * Action回调处理器
 */
public interface HandlerInvoker {

    Object invoke(ActionMapper mapper) throws Throwable;
}
