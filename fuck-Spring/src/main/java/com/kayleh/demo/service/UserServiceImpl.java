package com.kayleh.demo.service;

import com.kayleh.spring.Autowired;
import com.kayleh.spring.BeanNameAware;
import com.kayleh.spring.Component;
import com.kayleh.spring.InitializingBean;

/**
 * 业务类
 *
 * @Author: Kayleh
 * @Date: 2021/4/23 0:39
 */
//@Scope("prototype")
//@Lazy
@Component("UserService")
public class UserServiceImpl implements BeanNameAware, InitializingBean, UserService
{
    @Autowired
    private User user;

    private String beanName;//BeanNameAware

    private String userName;//user.getName();--->InitializingBean


    public void setBeanName(String beanName)
    {
        this.beanName = beanName;
    }

    public void test()
    {
        System.out.println(user);
        System.out.println(beanName);
    }

    public void afterPropertiesSet()
    {
        this.userName = user.getName();
    }
}
