package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.util.MyMapper;

import java.util.Map;

public interface TbPlanDetailMapper extends MyMapper<TbPlanDetail> {

    TbPlanDetail getPlanDetail(Integer planId);

    TbProjectPlan getProjectPlan(Integer planId);

    void insertPlanDetail(TbPlanDetail tbPlanDetail);

    void updatePlanDetail(TbPlanDetail tbPlanDetail);

    void finishPlanDetail(Map<String,Object> param);
}