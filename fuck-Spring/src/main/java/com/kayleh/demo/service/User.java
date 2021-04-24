package com.kayleh.demo.service;


import com.kayleh.spring.Component;

/**
 * pojo
 *
 * @Author: Kayleh
 * @Date: 2021/4/23 0:40
 */
@Component("user")
public class User
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
