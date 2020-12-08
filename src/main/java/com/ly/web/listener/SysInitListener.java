package com.ly.web.listener;

import com.ly.settings.domain.DicValue;
import com.ly.settings.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /**
     * 监听上下文域对象，当服务器启动，上下文域对象创建，对象创建完毕，立即执行该方法
     * servletContextEvent：能获取到监听的对象
     * */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("服务器缓存处理数据字典开始");

        String config = "applicationContext.xml";
        ApplicationContext ac = new ClassPathXmlApplicationContext(config);
        DicService dicService = (DicService) ac.getBean("dicService");

        ServletContext application = servletContextEvent.getServletContext();

        Map<String, List<DicValue>> map = dicService.getAll();
        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for(String key:set){
            application.setAttribute(key,map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");


        /**
         * 数据字典处理完毕后，处理Stage2Prossibility.properties配置文件：
         * 解析该文件，将文件中键值对关系处理成java中键值对关系（map）
         * */
        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        application.setAttribute("pMap",pMap);



    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
