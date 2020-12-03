package com.ly.workbench.controller;

import com.ly.settings.domain.User;
import com.ly.settings.service.UserService;
import com.ly.utils.DateTimeUtil;
import com.ly.utils.UUIDUtil;
import com.ly.workbench.domain.Activity;
import com.ly.workbench.domain.Clue;
import com.ly.workbench.service.ActivityService;
import com.ly.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/clue")
public class ClueController {

    @Resource
    private ClueService clueService;
    @Resource
    private UserService userService;
    @Resource
    private ActivityService activityService;


    //获取用户信息列表
    @RequestMapping(value = "getUserList.do",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserList(){
        System.out.println("获取用户信息列表");

        List<User> userList = userService.getUserList();

        return userList;
    }


    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    @ResponseBody
    public boolean save(Clue clue, HttpServletRequest request){
        System.out.println("执行线索添加操作");

        clue.setId(UUIDUtil.getUUID());
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);

        boolean flag = clueService.save(clue);
        return flag;

    }


    //跳转到线索详细信息页
    @RequestMapping(value = "detail.do")
    public ModelAndView detail(String id){
        System.out.println("跳转到线索详细信息页");

        ModelAndView mv = new ModelAndView();

        Clue clue = clueService.detail(id);

        mv.addObject("c",clue);
        mv.setViewName("forward:/workbench/clue/detail.jsp");

        return mv;

    }


    //根据线索id查询关联的市场活动列表
    @RequestMapping(value = "getActivityListByClueId.do",method = RequestMethod.GET)
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        System.out.println("根据线索id查询关联的市场活动列表");

        List<Activity> aList = activityService.getActivityListByClueId(clueId);

        return aList;
    }


    //解除关联操作
    @RequestMapping(value = "unbund.do",method = RequestMethod.POST)
    @ResponseBody
    public boolean unbund(String id){
        System.out.println("解除关联操作");

        boolean flag = clueService.unbund(id);
        return flag;
    }


    //查询市场活动列表(排除已经关联的指定线索列表+根据名称模糊查询)
    @RequestMapping(value = "getActivityListByNameAndNotByClueId.do",method = RequestMethod.GET)
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId){
        System.out.println("查询市场活动列表(排除已经关联的指定线索列表+根据名称模糊查询)");

        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);

        return aList;
    }



}
