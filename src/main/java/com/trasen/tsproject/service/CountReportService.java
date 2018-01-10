package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbOutputValueCountMapper;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.model.TbOutputValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/10
 */
@Service
public class CountReportService {

    @Autowired
    TbOutputValueCountMapper tbOutputValueCountMapper;

    public PageInfo<TbOutputValueCount> getcountReportList(Integer page,Integer rows,TbOutputValueCount tbOutputValueCount){
        PageHelper.startPage(page,rows);
        List<TbOutputValueCount> exceptionPlans=tbOutputValueCountMapper.getcountReportList(tbOutputValueCount);
        PageInfo<TbOutputValueCount> pagehelper = new PageInfo<TbOutputValueCount>(exceptionPlans);
        return pagehelper;
    }
}
