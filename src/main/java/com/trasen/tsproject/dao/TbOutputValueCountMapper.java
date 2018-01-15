package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.CountReportVo;
import com.trasen.tsproject.model.TbOutputValueCount;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbOutputValueCountMapper extends MyMapper<TbOutputValueCount> {

    List<TbOutputValueCount> getcountReportList(TbOutputValueCount tbOutputValueCount);

    List<TbOutputValueCount> getCountRByDept(Map<String,String> param);
    List<TbOutputValueCount> getCountRByPro(Map<String,String> param);
    List<TbOutputValueCount> getCountRByProline(Map<String,String> param);

    CountReportVo getUnfinishdByDept(String year);
    CountReportVo getUnfinishdByPro(String year);
    CountReportVo getUnfinishdByProline(String year);

    List<TbOutputValueCount> countOutputValue(String year);

    void insertOutputValue(TbOutputValueCount count);

    void updateOutputValue(TbOutputValueCount count);

    TbOutputValueCount getOutputValue(TbOutputValueCount count);



}