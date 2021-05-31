package com.kayleh.mapper;

import com.kayleh.bean.User;

import java.util.List;
import java.util.Map;

/**
 * @Author: Kayleh
 * @Date: 2021/4/28 0:32
 */
public interface UserMapper {

    public User getUserById(String id);

    List<User> getList(Map<String, Object> paramMap);
}
