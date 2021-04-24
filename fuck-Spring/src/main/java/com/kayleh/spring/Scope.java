package com.kayleh.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 懒加载
 *
 * @Author: Kayleh
 * @Date: 2021/4/23 0:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope
{
    //默认单例
    String value() default "singleton";
}
