package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtAnalyzeMapper extends MyMapper<TbHtAnalyze> {
    List<TbHtAnalyze> selectAnalyzeList(String htNo);

    int saveAnaly(TbHtAnalyze tbHtAnalyze);

    int deleteAnaly(String htNo);
}