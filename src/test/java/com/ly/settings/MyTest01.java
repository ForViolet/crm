package com.ly.settings;

import com.ly.utils.DateTimeUtil;
import com.ly.utils.MD5Util;
import org.junit.Test;

public class MyTest01 {

    @Test
    public void test01(){
        //测试验证失效时间
        String expireTime = "2020-12-12 10:10:10";
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println((count>0)?"未失效":"已失效");
    }


    @Test
    public void test02(){
        //验证密码加密
        String pwd = "123";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);
    }
}
