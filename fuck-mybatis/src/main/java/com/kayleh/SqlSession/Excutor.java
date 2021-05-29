package com.kayleh.SqlSession;

/**
 * @Author: Kayleh
 * @Date: 2021/5/5 23:49
 */
public interface Excutor
{
    public <T> T query(String sql, Object parameter);
}
