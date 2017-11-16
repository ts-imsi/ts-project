package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TwfStageDoc;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TwfStageDocMapper extends MyMapper<TwfStageDoc> {
    public List<TwfStageDoc> getTwfStageDocList(List<Integer> stageIdList);
}