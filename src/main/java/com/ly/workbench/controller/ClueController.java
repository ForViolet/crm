package com.ly.workbench.controller;

import com.ly.settings.domain.User;
import com.ly.settings.service.UserService;
import com.ly.utils.DateTimeUtil;
import com.ly.utils.UUIDUtil;
import com.ly.workbench.domain.Activity;
import com.ly.workbench.domain.Clue;
import com.ly.workbench.domain.Tran;
import com.ly.workbench.service.ActivityService;
import com.ly.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public List<Activity> getActivityListByNameAndNotByClueId(String aname,String clueId){
        System.out.println("查询市场活动列表(排除已经关联的指定线索列表+根据名称模糊查询)");

        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        List<Activity> aList = activityService.getActivityListByNameAndNotByClueId(map);

        return aList;
    }


    //执行关联市场活动操作
    @RequestMapping(value = "bund.do",method = RequestMethod.POST)
    @ResponseBody
    public boolean bund(HttpServletRequest request){
        System.out.println("执行关联市场活动操作");

        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");

        boolean flag = clueService.bund(cid,aids);

        return flag;
    }


    //查询市场活动列表(支持模糊查询)
    @RequestMapping(value = "getActivityListByName.do",method = RequestMethod.GET)
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        System.out.println("查询市场活动列表(支持模糊查询)");

        List<Activity> aList = activityService.getActivityListByName(aname);

        return aList;
    }


    //执行线索转换操作
    @RequestMapping(value = "convert.do")
    public void convert(HttpServletRequest request, HttpServletResponse response, String money, String name, String expectedDate, String stage, String activityId) throws IOException {
        System.out.println("执行线索转换操作");

        String clueId = request.getParameter("clueId");
        //接收是否需要创建表达的标记
        String flag = request.getParameter("flag");

        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = null;
        if("a".equals(flag)){
            t = new Tran();

            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
        }

        /**
         * 为业务层传递的参数：
         * 必须参数：clueId  明确要转换哪条记录
         * 必须参数：t  线索转换过程中，可能会临时创建一笔交易（业务层接受的t也可能为null）
         * */
        boolean result = clueService.convert(clueId,t,createBy);
        if(result){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

}
