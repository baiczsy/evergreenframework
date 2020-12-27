package org.evergreen.test;

import org.evergreen.aop.InvocationContext;
import org.evergreen.aop.annotation.Around;

/**
 * Created by chenjun on 2017/12/19.
 */
public class MyInterceptor {
    public  void before(){
        System.out.println("before 111 in myinterceptor");
    }

    public  void after(){
        System.out.println("after 111 in myinterceptor");
    }

    @Around
    public  void wrap(InvocationContext context) throws Throwable {
        System.out.println("before wrap in myinterceptor");
        context.proceed();
        System.out.println("after wrap in myinterceptor");
    }
}
