package org.evergreen.test;

import org.evergreen.aop.annotation.Interceptors;

/**
 * Created by chenjun on 2017/12/19.
 */


public class Handler implements HandlerInf{

    @Override
    @Interceptors({MyInterceptor.class})
    public  void handle(){
        System.out.println("处理事情中***");
    }
}
