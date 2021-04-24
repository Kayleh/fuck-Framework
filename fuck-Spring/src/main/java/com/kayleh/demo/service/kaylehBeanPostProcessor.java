package com.kayleh.demo.service;

import com.kayleh.spring.BeanPostProcessor;
import com.kayleh.spring.Component;

/**
 * @Author: Kayleh
 * @Date: 2021/4/24 14:34
 */
@Component("kaylehBeanPostProcessor")
public class kaylehBeanPostProcessor implements BeanPostProcessor
{
    public Object postProcessorBeforeInitialization(Object Bean, String beanName)
    {
        System.out.println("初始化之前");
        return new User();
    }

    public Object postProcessorAfterInitialization(Object Bean, String beanName)
    {
        System.out.println("初始化之后");
        return Bean;
    }
}
