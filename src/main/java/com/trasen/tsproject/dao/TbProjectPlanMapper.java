package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbProjectPlanMapper extends MyMapper<TbProjectPlan> {
    int saveProjectPlan(TbProjectPlan tbProjectPlan);
    List<TbProjectPlan> getProjectPlanByHandOverId(String handoverId);
    int updateProjectActualizePlan(TbProjectPlan tbProjectPlan);
    int selectCountManage(String handoverId);
}