package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.model.TbProjectPlanLog;
import com.trasen.tsproject.service.PlanDetailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @RequestMapping(value="/savePlanItemTime",method = RequestMethod.POST)
    public Result savePlanItemTime(@RequestBody List<TbPlanItem> planItems){
        Result result=new Result();
        try{
            if(!Optional.ofNullable(planItems).isPresent()){
                result.setMessage("传入参数错误");
                result.setSuccess(false);
            }else{
                planDetailService.updatePlanItemTime(planItems);
                result.setMessage("实施计划保存成功");
                result.setSuccess(true);
            }
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }

    @RequestMapping(value="/updatePlanTime",method = RequestMethod.POST)
    public Result updatePlanTime(@RequestBody TbPlanItem planItem){
        Result result=new Result();
        try{
            TbPlanItem item = planDetailService.updatePlanTime(planItem);
            result.setSuccess(true);
            result.setObject(item);
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }


    @RequestMapping(value="/{code}/updateLog/{planId}",method = RequestMethod.POST)
    public Result queryPlanUpdateLog(@PathVariable String code,@PathVariable Integer planId){
        Result result=new Result();
        result.setSuccess(false);
        try {
            List<TbProjectPlanLog> list = planDetailService.queryPlonTimechangeLog(code,planId,"actualizePlan");
            result.setObject(list);
            result.setSuccess(true);
        }catch (Exception e) {
            logger.error("查询初步计划时间修改历史记录异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查询初步计划时间修改历史记录失败");
        }
        return  result;

    }

    @RequestMapping(value="/check/ok",method = RequestMethod.POST)
    public Result checkOk(@RequestBody TbPlanItem planItem){
        Result result=new Result();
        result.setSuccess(false);
        try {
            boolean boo = planDetailService.checkOk(planItem);
            if(boo){
                result.setSuccess(true);
                result.setMessage("确认成功!");
            }else{
                result.setMessage("确认失败!");
            }

        }catch (Exception e) {
            logger.error("计划确认异常异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("计划确认异常");
        }
        return  result;
    }

    @RequestMapping(value="/check/back",method = RequestMethod.POST)
    public Result checkBack(@RequestBody TbPlanItem planItem){
        Result result=new Result();
        result.setSuccess(false);
        try {
            boolean boo = planDetailService.checkBack(planItem);
            if(boo){
                result.setSuccess(true);
                result.setMessage("驳回成功!");
            }else{
                result.setMessage("驳回失败!");
            }

        }catch (Exception e) {
            logger.error("计划驳回异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("计划驳回异常");
        }
        return  result;
    }


}
