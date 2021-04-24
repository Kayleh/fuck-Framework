package com.kayleh.spring;

/**
 * 执行初始化逻辑
 *
 * @Author: Kayleh
 * @Date: 2021/4/24 14:20
 */
public interface InitializingBean
{
    public void afterPropertiesSet();
}
