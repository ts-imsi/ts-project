package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TwfStage;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TwfStageMapper extends MyMapper<TwfStage> {
    List<TwfStage> selectTwfStageList();
}