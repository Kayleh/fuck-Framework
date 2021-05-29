package com.kayleh.SqlSession;

import java.lang.reflect.Proxy;

/**
 * mybatis核心
 * 会话
 * 一个Session仅拥有一个对应的数据库连接。
 * 它可以直接调用exec(SQL)来执行SQL语句。
 *
 * @Author: Kayleh
 * @Date: 2021/4/28 0:36
 */
public class SqlSession {
    private Excutor excutor;
    private MyConfiguration myConfiguration = new MyConfiguration();

    public <T> T selectOne(String statement, Object parameter) {
        return excutor.query(statement, parameter);
    }

    public <T> T getMappeer(Class<T> clas) {
        //动态代理调用
        return Proxy.newProxyInstance(clas.getClassLoader(), new Class[]{clas}, new MyMapperProxy(myConfiguration, this));
    }
}
