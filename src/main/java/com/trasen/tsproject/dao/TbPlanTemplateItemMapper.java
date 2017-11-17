package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanTemplateItem;
import com.trasen.tsproject.util.MyMapper;

public interface TbPlanTemplateItemMapper extends MyMapper<TbPlanTemplateItem> {
    int deletePlanItem(Integer tempId);
    int saveTemplateItem(TbPlanTemplateItem tbPlanTemplateItem);
}