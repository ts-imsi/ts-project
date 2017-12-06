package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.model.TbProModule;
import com.trasen.tsproject.model.TbProduct;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbProductMapper extends MyMapper<TbProduct> {
    List<TbProduct> selectProduct();
    List<TbHtModule> selectAddModuleView(String htNo);
    List<TbProduct> queryProductModelList(Map<String,String> param);
    int saveTbProduct(TbProduct tbProduct);
    int deleteTbProduct(Integer pkid);
    int updateTbProduct(TbProduct tbProduct);
}