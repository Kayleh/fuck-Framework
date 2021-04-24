package com.kayleh.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组件注解
 *
 * @Author: Kayleh
 * @Date: 2021/4/23 0:41
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component
{
    //指定bean的名字
    String value() default "";
}
