package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbOutputValueCountMapper;
import com.trasen.tsproject.model.CountReportVo;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.model.TbOutputValueCount;
import com.trasen.tsproject.util.DateUtils;
import org.apache.log4j.Logger;
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

    Logger logger = Logger.getLogger(CountReportService.class);

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

    public void countOutputValue(){
        String thisYear = DateUtils.getDate("yyyy");
        String lastYear = (Integer.parseInt(thisYear)-1)+"";
        List<TbOutputValueCount> list = tbOutputValueCountMapper.countOutputValue(thisYear);
        logger.info("======更新产值======开始");
        for(TbOutputValueCount count : list){
            count.setYear(thisYear);
            TbOutputValueCount valueCount = tbOutputValueCountMapper.getOutputValue(count);
            if(valueCount!=null){
                if(valueCount.getFinished().doubleValue() == count.getFinished().doubleValue()){
                    logger.info("更新产值[无更新]==合同["+count.getHtNo()+count.getHtName()+"]==产品["+count.getProName()+"]==["+count.getYear()+"]年==无更新===");
                }else{
                    tbOutputValueCountMapper.updateOutputValue(count);
                    logger.info("更新产值[更新]==合同["+count.getHtNo()+"]==产值["+count.getFinished()+"]==");
                }
            }else{
                count.setYear(lastYear);
                TbOutputValueCount last = tbOutputValueCountMapper.getOutputValue(count);
                if(last!=null){
                    last.setTotal(last.getUnfinished());
                    last.setFinished(count.getFinished());
                    last.setUnfinished(last.getUnfinished()-count.getFinished());
                    last.setYear(thisYear);
                    tbOutputValueCountMapper.insertOutputValue(last);
                    logger.info("更新产值[上年结转]==合同["+last.getHtNo()+"]==结转额["+last.getTotal()+"]==产值["+last.getFinished()+"]==");
                }else{
                    count.setYear(thisYear);
                    tbOutputValueCountMapper.insertOutputValue(count);
                    logger.info("更新产值[新合同]==合同["+count.getHtNo()+"]==分配额["+count.getTotal()+"]==产值["+count.getFinished()+"]==");
                }
            }
        }
        logger.info("======更新产值======结束");

    }

}
