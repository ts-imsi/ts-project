package com.trasen.tsproject.dao;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.util.MyMapper;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

public interface TbPlanItemMapper extends MyMapper<TbPlanItem> {
    List<ExceptionPlan> selectExceptionPlan(Map<String,Object> param);

    List<TbPlanItem> queryTemplatePlanItems(Map<String,Object> param);

    List<TbPlanItem> queryPlanItems(Map<String,Object> param);

    void insertPlanItem(TbPlanItem tbPlanItem);

    void updatePlanItemTime(TbPlanItem tbPlanItem);

    TbPlanItem getPlanItem(Integer pkid);

    void updatePlanItemDocFile(TbPlanItem tbPlanItem);

    String getUserRoleTag(String userId);

    void updateItemScore(TbPlanItem tbPlanItem);

    void planItemDocBack(TbPlanItem tbPlanItem);

    void updateItemComplete(TbPlanItem tbPlanItem);

    void updateCheck(Map<String,Object> param);

    void reUpload(Integer itemId);

    Double getPoit(Integer planId);

    void updatePlanPoit(Map<String,Object> param);

    Integer getHandoverIdToPlanId(Integer planId);

    Double getProPoit(Integer handId);

    void updateHandPoit(Map<String,Object> param);

    Integer getPlanStageCount(TbPlanItem item);

    Double getTwfStagePoit(TbPlanItem item);

    Map<String,Object> getPlanPoitFinsh(TbPlanItem item);

    List<String> sortStage();
}