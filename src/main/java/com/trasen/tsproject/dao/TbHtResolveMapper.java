package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtResolve;
import com.trasen.tsproject.util.MyMapper;

public interface TbHtResolveMapper extends MyMapper<TbHtResolve> {
    int saveHtResolve(TbHtResolve tbHtResolve);
    int deleteHtResolve(String htNo);
    int updateHtResolve(TbHtResolve tbHtResolve);
}