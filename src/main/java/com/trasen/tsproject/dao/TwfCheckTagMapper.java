package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TwfCheckTag;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TwfCheckTagMapper extends MyMapper<TwfCheckTag> {
    List<TwfCheckTag> selectCheckTag();
}