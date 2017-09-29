package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.model.TbHtResolve;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtModuleMapper extends MyMapper<TbHtModule> {
    List<TbHtModule> selectModuleByHtNo(String htNo);
    int saveHtModule(TbHtModule tbHtModule);
    int deleteHtModule(String htNo);
    List<TbHtModule> queryHtModule(TbHtResolve htResolve);

    int updateModulePrice(TbHtModule htModule);
}