package com.kayleh.spring;

/**
 * bean的后置处理器
 * 定义bean可以在初始化前或初始化后可以做的事情
 *
 * @Author: Kayleh
 * @Date: 2021/4/24 14:30
 */
public interface BeanPostProcessor
{
    Object postProcessorBeforeInitialization(Object Bean, String beanName);

    Object postProcessorAfterInitialization(Object Bean, String beanName);
}
