package org.evergreen.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解标注了@Inherited元注解，那么
 * 如果父类的自定义注解是定义在类上面，那么子类是可以继承过来的
 * 如果父类的自定义注解定义在方法上面，那么子类仍然可以继承过来
 * 如果子类重写了父类中定义了注解的方法，那么子类将无法继承该方法的注解
 * 即子类在重写父类中被@Inherited标注的方法时，会将该方法连带它上面的注解一并覆盖掉
 *
 * @Interceptor 自定义拦截器注解
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE,ElementType.METHOD}) 
@Inherited
public @interface Interceptors {
   public Class<?>[] value();
}
