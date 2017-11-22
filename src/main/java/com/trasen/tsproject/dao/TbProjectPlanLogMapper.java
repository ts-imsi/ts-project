package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.model.TbProjectPlanLog;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbProjectPlanLogMapper extends MyMapper<TbProjectPlanLog> {

    int insertPlanLog(TbProjectPlanLog tbProjectPlanLog);

    int updatePlanTime(TbProjectPlan tbProjectPlan);

    List<TbProjectPlanLog> queryPlanLogList(Map<String,Object> param);

    TbProjectPlan getPlanToId(Integer planId);

    List<TbProjectPlanLog> queryPlonTimechangeLog(Map<String,Object> param);



}