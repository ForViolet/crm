package com.ly.workbench.controller;

import com.ly.settings.domain.User;
import com.ly.settings.service.UserService;
import com.ly.utils.DateTimeUtil;
import com.ly.utils.UUIDUtil;
import com.ly.workbench.domain.Tran;
import com.ly.workbench.domain.TranHistory;
import com.ly.workbench.service.CustomerService;
import com.ly.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/workbench/transaction")
public class TranController {

    @Resource
    private TranService tranService;
    @Resource
    private UserService userService;
    @Resource
    private CustomerService customerService;


    //跳转到交易添加页操作
    @RequestMapping(value = "add.do")
    @ResponseBody
    private ModelAndView add(){
        System.out.println("跳转到交易添加页操作");

        ModelAndView mv = new ModelAndView();
        List<User> uList = userService.getUserList();
        System.out.println("uList:---------------"+uList);

        mv.addObject("uList",uList);
        mv.setViewName("forward:/workbench/transaction/save.jsp");
        return mv;
    }


    //取得客户名称列表
    @RequestMapping(value = "getCustomerName.do")
    @ResponseBody
    private List<String> getCustomerName(String name){
        System.out.println("取得客户名称列表(按客户名称进行模糊查询)");

        List<String> sList = customerService.getCustomerName(name);

        return sList;
    }


    //执行添加交易操作
    @RequestMapping(value = "save.do",method = RequestMethod.POST)
    private ModelAndView save(Tran tran, HttpServletRequest request){
        System.out.println("执行添加交易操作");

        ModelAndView mv = new ModelAndView();

        tran.setId(UUIDUtil.getUUID());
        tran.setCustomerId(null);

        String customerName = request.getParameter("customerName");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);

        mv.addObject("tran",tran);

        boolean flag = tranService.save(tran,customerName);

        if(flag){
            mv.setViewName("redirect:index.jsp");
        }

        return mv;
    }


    //跳转到详细信息页
    @RequestMapping(value = "detail.do")
    private ModelAndView detail(String id,HttpServletRequest request){
        System.out.println("跳转到详细信息页");

        ModelAndView mv = new ModelAndView();

        Tran tran = tranService.detail(id);

        //处理可能性
        String stage = tran.getStage();

        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(stage);

        tran.setPossibility(possibility);//对tran扩充，使possibility成为tran中一个属性

        mv.addObject("t",tran);

        mv.setViewName("forward:detail.jsp");

        return mv;

    }


    //根据交易id获取相应交易历史列表
    @RequestMapping(value = "getHistoryListByTranId.do")
    @ResponseBody
    private List<TranHistory> getHistoryListByTranId(String tranId,HttpServletRequest request){
        System.out.println("根据交易id获取相应交易历史列表");

        List<TranHistory> thList = tranService.getHistoryListByTranId(tranId);

        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");

        //遍历交易历史列表，取出每个阶段
        for(TranHistory th:thList){
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        return thList;

    }


    //执行改变阶段操作
    @RequestMapping(value = "changeStage.do",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> changeStage(String id,String stage,String money,String expectedDate,HttpServletRequest request){
        System.out.println("执行改变阶段操作");

        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setMoney(money);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        boolean flag = tranService.changeStage(t);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);

        return map;

    }


    //取得交易阶段数量统计图表的数据
    @RequestMapping(value = "getCharts.do")
    @ResponseBody
    private Map<String,Object> getCharts(){
        System.out.println("取得交易阶段数量统计图表的数据");

        Map<String,Object> map = tranService.getCharts();

        return map;

    }

}
