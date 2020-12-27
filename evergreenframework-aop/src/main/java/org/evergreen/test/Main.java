package org.evergreen.test;

import org.evergreen.aop.ProxyBuilder;

/**
 * Created by chenjun on 2017/12/19.
 */
public class Main {
    public static void main(String[] args) {
        ProxyBuilder proxyBuilder = new ProxyBuilder(Handler.class);
        HandlerInf handler = proxyBuilder.createProxy();
        handler.handle();
    }
}
