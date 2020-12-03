package com.ly.web.listener;

import com.ly.settings.domain.DicValue;
import com.ly.settings.service.DicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
