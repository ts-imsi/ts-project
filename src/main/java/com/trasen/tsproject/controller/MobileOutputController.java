package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/12/5
 */
@RequestMapping(value="/mobileOutput")
@RestController
public class MobileOutputController {

    private static final Logger logger = Logger.getLogger(MobileOutputController.class);

    @Autowired
    OutputValueService outputValueService;

    @RequestMapping(value="/quereyArrangeList",method = RequestMethod.POST)
    public Map<String,Object> quereyArrangeList(@RequestBody  Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        TbOutputValue outputValue = new TbOutputValue();
        try{
            if(param.isEmpty()){
                result.put("success",false);
                result.put("message","参数传入错误");
            }else{
                outputValue.setStatus(Integer.valueOf(param.get("status")));
                double total=0.0;
                List<OutputValueVo> outputValueVoList=outputValueService.selectOutputValueDept(outputValue);
                for(OutputValueVo outputValueVo:outputValueVoList){
                    total+=outputValueVo.getTotle();
                }
                result.put("success",true);
                result.put("list",outputValueVoList);
                result.put("total",total);
            }

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }

    @RequestMapping(value="/checkedOutput",method = RequestMethod.POST)
    public Result checkedOutput(@RequestBody List<TbOutputValue> outputList){
        Result result=new Result();
        try{
            if(Optional.ofNullable(outputList).isPresent()){
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
