package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProModulePrice;
import com.trasen.tsproject.model.TwfDict;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbProModulePriceMapper extends MyMapper<TbProModulePrice> {
    TbProModulePrice selectStandardPrice(TbProModulePrice tbStandardPrice);
    int updateStandardPrice(TbProModulePrice tbStandardPrice);
    int insertStandardPrice(TbProModulePrice tbStandardPrice);
    int deleteStandardPrice(Integer pkid);

    List<TbProModulePrice> queryModelPriceList(String modId);

}