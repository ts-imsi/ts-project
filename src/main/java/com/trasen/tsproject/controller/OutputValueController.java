package com.trasen.tsproject.controller;

import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.OutputValueVo;
import com.trasen.tsproject.model.TbOutputValue;
import com.trasen.tsproject.service.OutputValueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

            PageInfo<OutputValueVo> pageInfo = new PageInfo<>();
            TbOutputValue outputValue = new TbOutputValue();
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






}
