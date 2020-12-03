package com.ly.settings.service;

import com.ly.excep.LoginException;
import com.ly.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
