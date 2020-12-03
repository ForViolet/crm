package com.ly.settings.dao;

import com.ly.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User userLogin(Map<String,String> map);

    List<User> getUserList();
}
