package com.trasen.tsproject.controller;

import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.service.ExceptionPlanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/20
 */
@RestController
@RequestMapping(value="/exceptionPlan")
public class ExceptionPlanController {

    private static final Logger logger = Logger.getLogger(ExceptionPlanController.class);

    @Autowired
    private ExceptionPlanService exceptionPlanService;

    @RequestMapping(value = "/selectExceptionPlan",method = RequestMethod.POST)
    public Map<String,Object> selectExceptionPlan(@RequestBody Map<String,Object> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.isEmpty()){
                result.put("message","参数错误");
                result.put("success",false);
                return result;
            }
            if(!Optional.ofNullable(param.get("page")).isPresent()||!Optional.ofNullable(param.get("rows")).isPresent()){
                result.put("message","分页参数传入失败");
                result.put("success",false);
                return result;
            }
            PageInfo<ExceptionPlan> exceptionPlanpageInfo=exceptionPlanService.selectExceptionPlan(param);
            logger.info("数据查询条数"+exceptionPlanpageInfo.getList().size());
            result.put("totalPages",exceptionPlanpageInfo.getPages());
            result.put("pageNo",exceptionPlanpageInfo.getPageNum());
            result.put("totalCount",exceptionPlanpageInfo.getTotal());
            result.put("pageSize",exceptionPlanpageInfo.getPageSize());
            result.put("list",exceptionPlanpageInfo.getList());
            result.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("message","数据查询失败");
            result.put("success",false);
        }
        return result;
    }
}
