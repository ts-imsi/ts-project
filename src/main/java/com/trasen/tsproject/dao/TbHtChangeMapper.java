package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtChangeMapper extends MyMapper<TbHtChange> {
    List<TbHtChange> getHtChangeList(TbHtChange tbHtChange);

    int saveHtChange(TbHtChange tbHtChange);
}