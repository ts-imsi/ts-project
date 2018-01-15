package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
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

import java.util.*;

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
            if(Optional.ofNullable(param.get("year")).isPresent()&&!Optional.ofNullable(param.get("year")).get().equals("")){
                tbOutputValueCount.setYear(param.get("year"));
            }

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

    @RequestMapping(value="/getCountReport",method = RequestMethod.POST)
    public Result getCountReport(@RequestBody  Map<String,String> param){
        Result result=new Result();
        try{
            if(param.isEmpty()){
                result.setMessage("数据参数失败");
                result.setSuccess(false);
            }else{
                if(!param.get("selectType").equals("")&&param.get("selectType")!=null){
                    List<TbOutputValueCount> tbOutputValueCounts=new ArrayList<>();
                    if(param.get("selectType").equals("dept")){
                        tbOutputValueCounts=countReportService.getCountRByDept(param.get("year"));
                        result.setObject(tbOutputValueCounts);
                        result.setSuccess(true);
                    }else if(param.get("selectType").equals("pro")){
                        tbOutputValueCounts=countReportService.getCountRByPro(param.get("year"));
                        result.setObject(tbOutputValueCounts);
                        result.setSuccess(true);
                    }else if(param.get("selectType").equals("proLine")){
                        tbOutputValueCounts=countReportService.getCountRByProline(param.get("year"));
                        result.setObject(tbOutputValueCounts);
                        result.setSuccess(true);
                    }else{
                        result.setMessage("数据参数错误");
                        result.setSuccess(false);
                    }
                }else{
                    result.setSuccess(false);
                    result.setMessage("数据参数失败");
                }
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }
        return result;
    }

}
