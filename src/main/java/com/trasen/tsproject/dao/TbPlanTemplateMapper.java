package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanTemplate;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbPlanTemplateMapper extends MyMapper<TbPlanTemplate> {
    public List<TbPlanTemplate> queryPlanTemp();
    int deletePlanTemp(String proCode);
    int updatePlanTemp(TbPlanTemplate tbPlanTemplate);
    int savePlanTemp(TbPlanTemplate tbPlanTemplate);
    TbPlanTemplate selectPlanTemp(Integer pkid);
}