package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.Select;
import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtAnalyzeMapper extends MyMapper<TbHtAnalyze> {
    List<TbHtAnalyze> selectAnalyzeList(String htNo);

    String selectPersonJson(TbHtAnalyze tbHtAnalyze);

    List<Select> getSelectJson(TbHtAnalyze tbHtAnalyze);

    int saveAnaly(TbHtAnalyze tbHtAnalyze);

    int deleteAnaly(String htNo);
}