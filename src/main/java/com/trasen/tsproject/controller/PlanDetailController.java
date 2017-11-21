package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.service.PlanDetailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by zhangxiahui on 17/11/18.
 */
@RestController
@RequestMapping(value="/planDetail")
public class PlanDetailController {

    private static final Logger logger = Logger.getLogger(PlanDetailController.class);

    @Autowired
    PlanDetailService planDetailService;

    @RequestMapping(value="/queryPlanItems/{planId}",method = RequestMethod.POST)
    public Result queryPlanItems(@PathVariable Integer planId){
        Result result=new Result();
        result.setSuccess(false);
        try {
            TbPlanDetail TbPlanDetail = planDetailService.getPlanItemList(planId,"actualize");
            result.setSuccess(true);
            result.setObject(TbPlanDetail);
        }catch (Exception e) {
            logger.error("获取计划详情异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取计划详情异常");
        }
        return  result;
    }

    @RequestMapping(value="/savePlanDetail",method = RequestMethod.POST)
    public Result savePlanDetail(@RequestBody TbPlanDetail tbPlanDetail){
        Result result=new Result();
        try{
            if(Optional.ofNullable(tbPlanDetail).isPresent()){
                planDetailService.savePlanDetail(tbPlanDetail);
                result.setSuccess(true);
                result.setMessage("数据保存成功");
            }else{
                result.setSuccess(false);
                result.setMessage("参数参入错误");
            }
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }

}
