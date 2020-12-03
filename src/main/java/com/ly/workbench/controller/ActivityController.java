package com.ly.workbench.controller;

import com.ly.settings.domain.User;
import com.ly.settings.service.UserService;
import com.ly.utils.DateTimeUtil;
import com.ly.utils.PrintJson;
import com.ly.utils.UUIDUtil;
import com.ly.vo.PaginationVO;
import com.ly.workbench.domain.Activity;
import com.ly.workbench.domain.ActivityRemark;
import com.ly.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/activity")
public class ActivityController {

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


    //市场活动添加操作
    /*@RequestMapping(value = "save.do",method = RequestMethod.POST)
    public void save(HttpServletRequest request,HttpServletResponse response){
        System.out.println("执行市场活动添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间为当前系统时间·
        String createTime = DateTimeUtil.getSysTime();
        //创建人为当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateBy(createBy);
        a.setCreateTime(createTime);

        System.out.println("Activity a ="+a);

        boolean flag = activityService.save(a);
        System.out.println("flag="+flag);

        PrintJson.printJsonFlag(response,flag);


    }*/


    //执行市场活动添加操作
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    public ModelAndView save(Activity activity,HttpServletRequest request){
        System.out.println("执行市场活动添加操作");

        ModelAndView mv = new ModelAndView();

        String id = UUIDUtil.getUUID();
        //创建时间为当前系统时间·
        String createTime = DateTimeUtil.getSysTime();
        //创建人为当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setId(id);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);

        mv.addObject("id",activity.getId());
        mv.addObject("owner",activity.getOwner());
        mv.addObject("startDate",activity.getStartDate());
        mv.addObject("endDate",activity.getEndDate());
        mv.addObject("cost",activity.getCost());
        mv.addObject("description",activity.getDescription());
        mv.addObject("createBy",activity.getCreateBy());
        mv.addObject("createTime",activity.getCreateTime());

        System.out.println(activity);

        boolean flag = activityService.save(activity);

        mv.setViewName("index.jsp");
        return mv;
    }


    //进入到查询市场活动信息列表
    @RequestMapping(value = "pageList.do",method = RequestMethod.GET)
    public void pageList(HttpServletRequest request,HttpServletResponse response){
        System.out.println("进入到查询市场活动信息列表");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算出略过的记录条数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        PaginationVO<Activity> vo =  activityService.pageList(map);

        PrintJson.printJsonObj(response,vo);
    }


    //执行市场活动删除操作
    @RequestMapping(value = "delete.do",method = RequestMethod.POST)
    @ResponseBody
    public boolean delete(HttpServletRequest request){
        System.out.println("执行市场活动删除操作");

        String ids[] = request.getParameterValues("id");
        boolean flag = activityService.delete(ids);

        return  flag;
    }


    //查询用户信息列表和根据市场活动id查询单条记录操作
    @RequestMapping(value = "getUserListAndActivity.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){
        System.out.println("查询用户信息列表和根据市场活动id查询单条记录操作");

        Map<String,Object> map = activityService.getUserListAndActivity(id);

        return map;
    }


    //执行市场活动修改操作
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    public ModelAndView update(Activity activity,HttpServletRequest request){
        System.out.println("执行市场活动修改操作");

        ModelAndView mv = new ModelAndView();

        String id = request.getParameter("id");
        //修改时间为当前系统时间·
        String editTime = DateTimeUtil.getSysTime();
        //修改人为当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setId(id);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        mv.addObject("id",id);
        mv.addObject("owner",activity.getOwner());
        mv.addObject("name",activity.getName());
        mv.addObject("startDate",activity.getStartDate());
        mv.addObject("endDate",activity.getEndDate());
        mv.addObject("cost",activity.getCost());
        mv.addObject("description",activity.getDescription());
        mv.addObject("editBy",activity.getEditBy());
        mv.addObject("editTime",activity.getEditTime());

        System.out.println(activity);

        boolean flag = activityService.update(activity);
        System.out.println("flag:"+flag);

        mv.setViewName("index.jsp");
        return mv;
    }


    //进入到跳转到详细信息页操作
    @RequestMapping(value = "detail.do")
    public ModelAndView detail(String id){
        System.out.println("进入到跳转到详细信息页操作");

        ModelAndView mv = new ModelAndView();

        Activity activity = activityService.detail(id);

        mv.addObject("a",activity);
        mv.setViewName("forward:/workbench/activity/detail.jsp");

        return mv;

    }


    //根据市场活动id，取得备注信息列表
    @RequestMapping(value = "getRemarkListByAid.do",method = RequestMethod.GET)
    @ResponseBody
    public List<ActivityRemark> getRemarkListByAid(String activityId){
        System.out.println("根据市场活动id，取得备注信息列表");

        List<ActivityRemark> arList = activityService.getRemarkListByAid(activityId);

        return arList;
    }


    //删除备注操作
    @RequestMapping(value = "deleteRemark.do",method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteRemark(String id){
        System.out.println("删除备注操作");

        boolean flag = activityService.deleteRemark(id);

        return flag;

    }


    //执行添加备注操作
    @RequestMapping(value = "saveRemark.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> saveRemark(HttpServletRequest request){
        System.out.println("执行添加备注操作");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        boolean flag = activityService.saveRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);

        return map;

    }


    //执行修改备注操作
    @RequestMapping(value = "updateRemark.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> updateRemark(HttpServletRequest request){
        System.out.println("执行修改备注操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);
        ar.setEditBy(editBy);

        boolean flag = activityService.updateRemark(ar);

        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);

        return map;

    }

}
