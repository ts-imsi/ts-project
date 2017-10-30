package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtModuleChange;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtModuleChangeMapper extends MyMapper<TbHtModuleChange> {
    int saveHtModuleChange(TbHtModuleChange tbHtModuleChange);
    List<String> selectOldModuleList(String htNo);
    List<String> selectNewModuleList(String htNo);
}