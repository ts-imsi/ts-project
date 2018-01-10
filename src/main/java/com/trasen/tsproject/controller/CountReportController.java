package com.trasen.tsproject.controller;

import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.model.TbOutputValueCount;
import com.trasen.tsproject.service.CountReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zhangxiahui on 18/1/9.
 */
@RestController
@RequestMapping(value="/countReport")
public class CountReportController {

    private static final Logger logger = Logger.getLogger(CountReportController.class);

    @Autowired
    CountReportService countReportService;

    @RequestMapping(value="/getcountReportList",method = RequestMethod.POST)
    public Map<String,Object> getcountReportList(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<>();
        try{
            if(param.isEmpty()){
                paramMap.put("success",false);
                paramMap.put("message","参数参入错误");
                return paramMap;
            }
            if(!Optional.ofNullable(param.get("page")).isPresent()||!Optional.ofNullable(param.get("rows")).isPresent()){
                paramMap.put("message","分页参数传入失败");
                paramMap.put("success",false);
                return paramMap;
            }
            TbOutputValueCount tbOutputValueCount=new TbOutputValueCount();
            PageInfo<TbOutputValueCount> valueCountPageInfo=countReportService.getcountReportList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbOutputValueCount);
            logger.info("数据查询条数"+valueCountPageInfo.getList().size());
            paramMap.put("totalPages",valueCountPageInfo.getPages());
            paramMap.put("pageNo",valueCountPageInfo.getPageNum());
            paramMap.put("totalCount",valueCountPageInfo.getTotal());
            paramMap.put("pageSize",valueCountPageInfo.getPageSize());
            paramMap.put("list",valueCountPageInfo.getList());
            paramMap.put("success",true);

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            paramMap.put("success",false);
            paramMap.put("message","数据查询失败");
        }
        return paramMap;
    }

}
