package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbOutputValueMapper extends MyMapper<TbOutputValue> {

    TbProjectPlan getProjectPlan(Integer planId);

    TbHtHandover getHtHandOver(String handoverId);

    TbHtResolve getHtResolve(Map<String,Object> param);

    TbProduct getProduct(String proCode);

    void insertOutputValue(TbOutputValue outputValue);

    void updateOutputValue(TbOutputValue outputValue);

    List<OutputValueVo> queryOutputValueToDept(TbOutputValue outputValue);

    List<OutputValueVo> queryOutputValueToHT(TbOutputValue outputValue);

    List<TbOutputValue> queryOutputValue(TbOutputValue outputValue);


}