package com.kayleh.demo;

import com.kayleh.spring.ApplicationContext;

/**
 * 启动类
 *
 * @Author: Kayleh
 * @Date: 2021/4/23 0:15
 */
public class Test
{
    public static void main(String[] args)
    {
        //启动Spring 扫描--->  创建非懒加载单例Bean
        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        //getBean  原型，懒加载单例
        Object userService = applicationContext.getBean("UserService");
        //System.out.println(userService);
    }
}
