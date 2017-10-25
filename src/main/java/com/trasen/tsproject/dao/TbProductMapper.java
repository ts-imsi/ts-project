package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProduct;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbProductMapper extends MyMapper<TbProduct> {
    List<TbProduct> selectProduct();
}