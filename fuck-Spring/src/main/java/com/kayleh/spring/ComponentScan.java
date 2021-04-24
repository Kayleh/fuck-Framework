package com.kayleh.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Kayleh
 * @Date: 2021/4/23 0:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan
{
    //指定你要扫描的包的路径
    String value() default "";


}
