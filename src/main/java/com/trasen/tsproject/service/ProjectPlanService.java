package com.trasen.tsproject.service;

import com.trasen.tsproject.dao.TbProjectPlanMapper;
import com.trasen.tsproject.model.TbProjectPlan;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiahui on 17/11/1.
 */
@Service
public class ProjectPlanService {

    Logger logger = Logger.getLogger(ProjectPlanService.class);

    @Autowired
    TbProjectPlanMapper tbProjectPlanMapper;


    /**
     * 获取项目计划初始划数据
     *
     * */
    public List<TbProjectPlan> queryProjectPlanList(){
        List<TbProjectPlan> list = new ArrayList<>();
        return list;
    }










}
