package com.ly.workbench.service.impl;

import com.ly.workbench.dao.ClueActivityRelationDao;
import com.ly.workbench.dao.ClueDao;
import com.ly.workbench.domain.Clue;
import com.ly.workbench.service.ClueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ClueServiceImpl implements ClueService {

    @Resource
    private ClueDao clueDao;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao;

    @Override
    public boolean save(Clue clue) {
        boolean flag = true;

        int count = clueDao.save(clue);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);

        return clue;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);
        if(count != 1){
            flag = false;
        }
        return flag;
    }
}
