package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtModuleChange;
import com.trasen.tsproject.util.MyMapper;

public interface TbHtModuleChangeMapper extends MyMapper<TbHtModuleChange> {
    int saveHtModuleChange(TbHtModuleChange tbHtModuleChange);
}