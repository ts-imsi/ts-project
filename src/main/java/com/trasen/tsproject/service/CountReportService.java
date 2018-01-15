package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbOutputValueCountMapper;
import com.trasen.tsproject.model.CountReportVo;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.model.TbOutputValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public List<TbOutputValueCount> getCountRByDept(String year){
        Integer lastYear=Integer.valueOf(year)-1;
        Map<String,String> param=new HashMap<>();
        param.put("year",year);
        param.put("lastYear",lastYear.toString());
        List<TbOutputValueCount> tbOutputValueCounts= tbOutputValueCountMapper.getCountRByDept(param);
        tbOutputValueCounts.stream().forEach(tbOutputValueCount->{

            tbOutputValueCount.setTotal(tbOutputValueCount.getTotal()- Optional.ofNullable(tbOutputValueCount.getLastUnFinished()).orElse(0.0));

        });
        return tbOutputValueCounts;
    }

    public List<TbOutputValueCount> getCountRByPro(String year){
        Integer lastYear=Integer.valueOf(year)-1;
        Map<String,String> param=new HashMap<>();
        param.put("year",year);
        param.put("lastYear",lastYear.toString());
        List<TbOutputValueCount> tbOutputValueCounts= tbOutputValueCountMapper.getCountRByPro(param);
        tbOutputValueCounts.stream().forEach(tbOutputValueCount->{
            tbOutputValueCount.setTotal(tbOutputValueCount.getTotal()- Optional.ofNullable(tbOutputValueCount.getLastUnFinished()).orElse(0.0));
        });
        return tbOutputValueCounts;
    }
    public List<TbOutputValueCount> getCountRByProline(String year){
        Integer lastYear=Integer.valueOf(year)-1;
        Map<String,String> param=new HashMap<>();
        param.put("year",year);
        param.put("lastYear",lastYear.toString());
        List<TbOutputValueCount> tbOutputValueCounts= tbOutputValueCountMapper.getCountRByProline(param);
        tbOutputValueCounts.stream().forEach(tbOutputValueCount->{
            tbOutputValueCount.setTotal(tbOutputValueCount.getTotal()- Optional.ofNullable(tbOutputValueCount.getLastUnFinished()).orElse(0.0));
        });
        return tbOutputValueCounts;
    }

}
