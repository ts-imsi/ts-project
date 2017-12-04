package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.OutputValueVo;
import com.trasen.tsproject.model.TbOutputValue;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.service.OutputValueService;
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
 * Created by zhangxiahui on 17/12/1.
 */
@RestController
@RequestMapping(value="/outputValue")
public class OutputValueController {

    private static final Logger logger = Logger.getLogger(OutputValueController.class);


    @Autowired
    OutputValueService outputValueService;


    @RequestMapping(value="/queryOutputValue",method = RequestMethod.POST)
    public Map<String,Object> selectProjectArrangeList(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.isEmpty()||param.get("rows")==null||param.get("page")==null){
                result.put("success",false);
                result.put("message","参数为空");
            }

            Integer status = null;

            if(param.get("status")!=null){
                status = Integer.valueOf(param.get("status"));
            }


            PageInfo<OutputValueVo> pageInfo = new PageInfo<>();
            TbOutputValue outputValue = new TbOutputValue();
            outputValue.setStatus(status);
            if(param.get("type")!=null){
                if("dept".equals(param.get("type"))){
                    outputValue.setDepName(param.get("name"));
                    pageInfo = outputValueService.queryOutputValueDept(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),outputValue);
                }else if("ht".equals(param.get("type"))){
                    outputValue.setHtName(param.get("name"));
                    pageInfo = outputValueService.queryOutputValueHT(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),outputValue);
                }
            }
            logger.info("数据查询条数"+pageInfo.getList().size());
            result.put("totalPages",pageInfo.getPages());
            result.put("pageNo",pageInfo.getPageNum());
            result.put("totalCount",pageInfo.getTotal());
            result.put("pageSize",pageInfo.getPageSize());
            result.put("list",pageInfo.getList());
            result.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }


    @RequestMapping(value="/checkOutput",method = RequestMethod.POST)
    public Result checkOutput(@RequestBody List<TbOutputValue> outputList){
        Result result=new Result();
        try{
            if(outputList!=null){
                outputValueService.updateOutputValue(outputList);
                result.setSuccess(true);
                result.setMessage("产值确认成功!");
            }else{
                result.setSuccess(false);
                result.setMessage("参数参入错误");
            }
        }catch (Exception e){
            logger.error("产值确认失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("产值确认失败!");
        }
        return result;
    }






}
