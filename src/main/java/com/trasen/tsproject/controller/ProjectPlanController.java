package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.model.TbProjectPlanLog;
import com.trasen.tsproject.service.ProjectPlanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangxiahui on 17/11/1.
 */
@RestController
@RequestMapping(value="/plan")
public class ProjectPlanController {

    private static final Logger logger = Logger.getLogger(ProjectPlanController.class);

    @Autowired
    public ProjectPlanService projectPlanService;


    @RequestMapping(value="/{code}/updateLog/{planId}",method = RequestMethod.POST)
    public Result queryPlanUpdateLog(@PathVariable String code,@PathVariable Integer planId){
        Result result=new Result();
        result.setSuccess(false);
        try {
            List<TbProjectPlanLog> list = projectPlanService.queryPlanUpdateLog(code,planId);
            result.setObject(list);
            result.setSuccess(true);
        }catch (Exception e) {
            logger.error("查询初步计划时间修改历史记录异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查询初步计划时间修改历史记录失败");
        }
        return  result;

    }

    /**
     * 提交交接单
     * */
    @RequestMapping(value="/updatePlanTime", method = RequestMethod.POST)
    public Result updatePlanTile(@RequestBody TbProjectPlan plan)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            boolean boo = projectPlanService.updatePlanTime(plan);
            result.setSuccess(boo);
            result.setObject(plan);
        }catch (Exception e) {
            logger.error("提交交接单异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("提交交接单失败");
        }
        return  result;
    }










}
