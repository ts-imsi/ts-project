package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbPlanItemMapper extends MyMapper<TbPlanItem> {

    List<TbPlanItem> queryTemplatePlanItems(Map<String,Object> param);

    List<TbPlanItem> queryPlanItems(Map<String,Object> param);

    void insertPlanItem(TbPlanItem tbPlanItem);
}