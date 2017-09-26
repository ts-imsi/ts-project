package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbTemplateItem;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbTemplateItemMapper extends MyMapper<TbTemplateItem> {

    List<TbTemplateItem> queryItemList(String type);
}