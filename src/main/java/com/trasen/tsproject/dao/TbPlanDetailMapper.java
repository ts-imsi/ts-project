package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.util.MyMapper;

public interface TbPlanDetailMapper extends MyMapper<TbPlanDetail> {

    TbPlanDetail getPlanDetail(Integer planId);

    TbProjectPlan getProjectPlan(Integer planId);

    void insertPlanDetail(TbPlanDetail tbPlanDetail);

    void updatePlanDetail(TbPlanDetail tbPlanDetail);

    void finishPlanDetail(Integer planId);
}