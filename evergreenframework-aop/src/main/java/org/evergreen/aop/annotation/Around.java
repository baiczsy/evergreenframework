package org.evergreen.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 环绕通知注解
 * @author lenovo
 *
 */

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) 
public @interface Around {}
