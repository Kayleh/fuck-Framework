package com.kayleh.spring;

/**
 * bean定义
 *
 * @Author: Kayleh
 * @Date: 2021/4/23 12:12
 */
public class BeanDefinition
{
    private Class beanClass;
    //对应的Scope
    private String scope;
    //是否是懒加载
    private Boolean isLazy;

    public Class getBeanClass()
    {
        return beanClass;
    }

    public void setBeanClass(Class beanClass)
    {
        this.beanClass = beanClass;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public Boolean getLazy()
    {
        return isLazy;
    }

    public void setLazy(Boolean lazy)
    {
        isLazy = lazy;
    }
}
