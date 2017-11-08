package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProModule;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbProModuleMapper extends MyMapper<TbProModule> {
    List<TbProModule> selectProModule(String proCode);
    List<String> selectCleckModule(String htNo);
    TbProModule selectProCode(String modId);
    List<String> getOldTbProModule(String htNo);

    TbProModule selectProductCount(String pkid);
    void saveProduct(TbProModule tbProduct);
    List<TbProModule> queryProModuleList(List<String> proCodeList);
}