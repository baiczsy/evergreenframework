package org.evergreen.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {
	/**
	 * Bean的创建方式,默认是单例,还有原型(prototype)两种方式
	 * @return
	 */
	String value() default ScopeType.SINGLETON;
}
