package org.evergreen.web;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Action回调处理器
 */
public interface HandlerInvoker {

    Object invoke(ActionMapper mapper) throws IOException, ServletException;
}
