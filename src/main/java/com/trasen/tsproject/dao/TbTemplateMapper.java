package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbTemplate;
import com.trasen.tsproject.util.MyMapper;

public interface TbTemplateMapper extends MyMapper<TbTemplate> {

    TbTemplate getTemplate(Integer tid);
}