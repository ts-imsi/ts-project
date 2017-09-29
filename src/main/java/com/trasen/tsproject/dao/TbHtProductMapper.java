package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtProduct;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtProductMapper extends MyMapper<TbHtProduct> {
    TbHtProduct selectTbHtProductById(String pkid);

    List<TbHtProduct> selectTbHtProductByHtNo(String htNo);
    int saveTbHtProduct(TbHtProduct tbHtProduct);


    int updateTbHtProduct(TbHtProduct tbHtProduct);
}