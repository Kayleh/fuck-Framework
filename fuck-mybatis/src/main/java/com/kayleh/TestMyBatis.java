package com.kayleh;

import com.kayleh.SqlSession.SqlSession;
import com.kayleh.bean.User;
import com.kayleh.mapper.UserMapper;

/**
 * @Author: Kayleh
 * @Date: 2021/4/28 0:36
 */
public class TestMyBatis {
    public static void main(String[] args) {
        SqlSession sqlsession = new SqlSession();
        UserMapper mapper = sqlsession.getMappeer(UserMapper.class);
        User user = mapper.getUserById("1");
        System.out.println(user);
    }
}
