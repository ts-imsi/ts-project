package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProModulePrice;
import com.trasen.tsproject.util.MyMapper;

public interface TbProModulePriceMapper extends MyMapper<TbProModulePrice> {
    TbProModulePrice selectStandardPrice(TbProModulePrice tbStandardPrice);
}