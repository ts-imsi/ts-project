package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.service.PlanDetailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
