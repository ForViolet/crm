package com.ly.workbench.service.impl;

import com.ly.utils.DateTimeUtil;
import com.ly.utils.UUIDUtil;
import com.ly.workbench.dao.CustomerDao;
import com.ly.workbench.dao.TranDao;
import com.ly.workbench.dao.TranHistoryDao;
import com.ly.workbench.domain.Customer;
import com.ly.workbench.domain.Tran;
import com.ly.workbench.domain.TranHistory;
import com.ly.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;
    @Resource
    private CustomerDao customerDao;


    @Override
    public boolean save(Tran tran, String customerName) {

        /**
         * 交易添加操作：
         * 先处理客户相关需求
         * 1.处理customerName，根据客户名称精确查询，
         *   客户存在，则取出客户id，封装到tran对象中
         *   客户不能存在，则新建一条客户信息，将客户id取出风转到tran对象中
         * 2.执行添加交易操作
         * 3.创建一条交易历史
         * */

        boolean flag = true;


        Customer customer = customerDao.getCustomerByName(customerName);
        //如果customer为空，创建一个新用户
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setOwner(tran.getOwner());
            customer.setNextContactTime(tran.getNextContactTime());
            //添加客户
            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }
        }

        //将客户id封装到tran对象中
        tran.setCustomerId(customer.getId());

        //添加交易
        int count2 = tranDao.save(tran);
        if(count2 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(tran.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3 != 1){
            flag = false;
        }


        return flag;
    }


    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }


    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }


    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;

        //改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1 != 1){
            flag = false;
        }

        //交易阶段改变后，生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);

        return flag;
    }


    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList保存到map中
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);

        //将map返回
        return map;
    }
}
