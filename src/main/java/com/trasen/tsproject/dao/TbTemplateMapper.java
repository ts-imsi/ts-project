package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbTemplate;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbTemplateMapper extends MyMapper<TbTemplate> {

    TbTemplate getTemplate(String type);

    List<TbTemplate> getTemplateList(TbTemplate tbTemplate);

    int updateTemplate(TbTemplate tbTemplate);

    int insertTemplate(TbTemplate tbTemplate);
}