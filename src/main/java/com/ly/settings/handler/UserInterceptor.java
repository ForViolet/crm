package com.ly.settings.handler;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {

    //验证登录用户信息
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("UserInterceptor中的preHandler()");

        Object attr = request.getSession().getAttribute("user");
        System.out.println("attr-------"+attr);

        if(attr == null){
            System.out.println("用户未登录，跳转到登录页面");
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return false;

            /**
             * 实际项目开发过程中，对路径的使用，应一律使用绝对路径
             * 关于重定向和转发路径的写法：
             * 请求转发：使用一种特殊的绝对路径的写法，路径前不用加/项目名，也称之为内部路径
             *         /login.jsp
             * 重定向：使用传统绝对路径的写法，必须以/项目名开头，后面跟具体的资源路径
             *         /crm/login.jsp
             *
             * 为什么使用重定向，不使用请求转发
             *      请求转发之后，路径会停留在老路径上，而不是跳转之后最新资源的路径
             *      应该在为用户跳转到登录页的同时，浏览器地址栏自动设置为当前登录页的路径
             * */
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("UserInterceptor中的postHandler()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("UserInterceptor中的afterCompletion()");
    }
}
