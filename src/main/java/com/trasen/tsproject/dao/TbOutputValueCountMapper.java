package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbOutputValueCount;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbOutputValueCountMapper extends MyMapper<TbOutputValueCount> {

    List<TbOutputValueCount> getcountReportList(TbOutputValueCount tbOutputValueCount);

}