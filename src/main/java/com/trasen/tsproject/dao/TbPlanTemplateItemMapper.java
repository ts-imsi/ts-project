package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanTemplateItem;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbPlanTemplateItemMapper extends MyMapper<TbPlanTemplateItem> {
    int deletePlanItem(Integer tempId);
    int saveTemplateItem(TbPlanTemplateItem tbPlanTemplateItem);
    List<TbPlanTemplateItem> selectPlanItem(Integer tempId);
}