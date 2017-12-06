package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.OutputValueVo;
import com.trasen.tsproject.model.TbOutputValue;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.service.OutputValueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
                }else if("proLine".equals(param.get("type"))){
                    outputValue.setHtName(param.get("name"));
                    pageInfo = outputValueService.queryOutputValueToProLine(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),outputValue);
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


    @RequestMapping(value="/queryProLine",method = RequestMethod.POST)
    public Result queryProLine(){
        Result result=new Result();
        result.setSuccess(false);
        try {
            List<TbOutputValue> list = outputValueService.queryProLine();
            result.setObject(list);
            result.setSuccess(true);
        }catch (Exception e) {
            logger.error("查询产品线异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查询产品线异常");
        }
        return  result;
    }


    @RequestMapping(value="/saveNoHtOutput",method = RequestMethod.POST)
    public Result saveNoHtOutput(@RequestBody TbOutputValue outputValue){
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("产值添加失败!");
        try{
            if(outputValue!=null&&outputValue.getProLine()!=null&&outputValue.getTotal()!=null){
                outputValue.setStatus(1);
                outputValue.setSubtotal(outputValue.getTotal());
                outputValue.setOperator(VisitInfoHolder.getShowName());
                outputValue.setDocName(outputValue.getRemark());
                outputValue.setOutput(1d);
                List<TbOutputValue> list = new ArrayList<>();
                list.add(outputValue);
                outputValueService.insertOutputValue(list);
                result.setSuccess(true);
                result.setMessage("产值添加成功!");

            }
        }catch (Exception e){
            logger.error("保存无合同产值失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("保存无合同产值失败!");
        }
        return result;
    }


    @RequestMapping(value="/queryHtProduct",method = RequestMethod.POST)
    public Result queryHtProduct(@RequestBody Map<String,String> map){
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(map!=null&&map.get("htNo")!=null){
                List<TbOutputValue> list = outputValueService.queryHtProduct(map.get("htNo"));
                result.setObject(list);
                result.setSuccess(true);
            }
        }catch (Exception e) {
            logger.error("查询合同产值异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查询合同产值异常");
        }
        return  result;
    }

    @RequestMapping(value="/saveHtOutput",method = RequestMethod.POST)
    public Result saveHtOutput(@RequestBody List<TbOutputValue> outputList){
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("产值添加失败!");
        try{
            if(outputList!=null&&outputList.size()>0){
                String htNo = outputList.get(0).getHtNo();
                Integer count = outputValueService.findOutputToHtNo(htNo);
                if(count>0){
                    result.setSuccess(false);
                    result.setMessage("该合同已经添加产值,不可重复添加!");
                    return result;
                }
                //判断是否重新添加
                for(TbOutputValue outputValue : outputList){
                    outputValue.setStatus(1);
                    outputValue.setSubtotal(outputValue.getTotal());
                    outputValue.setOperator(VisitInfoHolder.getShowName());
                    outputValue.setDocName(outputValue.getRemark());
                    outputValue.setOutput(1d);
                }
                outputValueService.insertOutputValue(outputList);
                result.setSuccess(true);
                result.setMessage("产值添加成功!");
            }
        }catch (Exception e){
            logger.error("保存有合同产值失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("保存有合同产值失败!");
        }
        return result;
    }






}
