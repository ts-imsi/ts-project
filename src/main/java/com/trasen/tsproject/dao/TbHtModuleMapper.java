package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtModuleMapper extends MyMapper<TbHtModule> {
    List<TbHtModule> selectModuleByHtNo(String htNo);
    int saveHtModule(TbHtModule tbHtModule);
    int deleteHtModule(String htNo);
}