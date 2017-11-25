package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPlanCheck;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.model.TwfCheckTag;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbPlanCheckMapper extends MyMapper<TbPlanCheck> {

    void insertPlanCheck(TbPlanCheck tbPlanCheck);

    TwfCheckTag getCheckTag(String tagId);

    List<TbPlanCheck> queryPlanCheck(TbPlanItem tbPlanItem);
}