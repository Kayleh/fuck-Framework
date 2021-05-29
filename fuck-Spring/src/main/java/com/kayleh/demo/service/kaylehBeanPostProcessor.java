package com.kayleh.demo.service;

import com.kayleh.spring.BeanPostProcessor;
import com.kayleh.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

    public Object postProcessorAfterInitialization(final Object Bean, String beanName)
    {
        System.out.println("初始化之后");
        //匹配
        if (beanName.equals("userService"))
        {
            Object proxyInstance = Proxy.newProxyInstance(BeanPostProcessor.class.getClassLoader(), Bean.getClass().getInterfaces(), new InvocationHandler()
            {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                {
                    System.out.println("代理逻辑");
                    return method.invoke(Bean, args);//业务逻辑
                }
            });
            return proxyInstance;
        }
        return Bean;
    }
}
