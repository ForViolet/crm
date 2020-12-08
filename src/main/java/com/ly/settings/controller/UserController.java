package com.ly.settings.controller;

import com.ly.settings.domain.User;
import com.ly.settings.service.UserService;
import com.ly.utils.MD5Util;
import com.ly.utils.PrintJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/settings/user")
public class UserController {

    @Resource
    private UserService service;


    //登录验证操作
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    public void login(String loginAct,String loginPwd,HttpServletRequest request, HttpServletResponse response){
        System.out.println("登录验证操作");

        loginPwd = MD5Util.getMD5(loginPwd);//密码加密
        String ip = request.getRemoteAddr();

        try{
            User user = service.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            //没有抛出异常，表示登录成功
            PrintJson.printJsonFlag(response,true);
        } catch (Exception e){
            e.printStackTrace();

            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }

    }

}
