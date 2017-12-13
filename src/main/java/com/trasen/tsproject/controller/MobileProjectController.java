package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.service.PlanDetailService;
import com.trasen.tsproject.service.ProjectArrangeService;
import com.trasen.tsproject.service.ProjectPlanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/30
 */
@RestController
@RequestMapping(value="/mobileProject")
public class MobileProjectController {

    private static final Logger logger = Logger.getLogger(MobileProjectController.class);

    @Autowired
    ProjectArrangeService projectArrangeService;

    @Autowired
    PlanDetailService planDetailService;

    @Autowired
    ProjectPlanService projectPlanService;

    @RequestMapping(value="/selectMobileProjectArrangeList",method = RequestMethod.POST)
    public Result selectMobileProjectArrangeList(@RequestBody Map<String,String> param){
        Result result=new Result();
        try{
            TbHtHandover htHandover=new TbHtHandover();
            Optional<String> opS=Optional.ofNullable(param.get("isArrange"));
            if(opS.isPresent()&&!opS.get().equals("")){
                htHandover.setIsArrange(Integer.valueOf(opS.get()));
            }

            if(param.get("showAll")==null||!"all".equals(param.get("showAll"))){
                //列表权限
                htHandover.setHtOwner(VisitInfoHolder.getShowName());
            }

            List<TbHtHandover> tbHtHandoverList=projectArrangeService.selectMobileProjectArrangeList(htHandover);
            result.setObject(tbHtHandoverList);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/selectMobilePlanItems/{planId}",method = RequestMethod.POST)
    public Map<String,Object> selectMobilePlanItems(@PathVariable Integer planId){
        Map<String,Object> result=new HashMap<>();
        try {
            TbPlanDetail tbPlanDetail = planDetailService.getPlanItemList(planId,"actualize");
            TbHtHandover htHandover=projectPlanService.selectHandOverByPlanId(planId);
            TbProjectPlan tbProjectPlan=projectPlanService.queryProjectByPlanId(planId);
            result.put("handover",htHandover);
            result.put("planDetail",tbPlanDetail);
            result.put("tbProjectPlan",tbProjectPlan);
            result.put("success",true);
        }catch (Exception e) {
            logger.error("获取计划详情异常" + e.getMessage(), e);
            result.put("success",false);
            result.put("message","获取计划详情异常");
        }
        return  result;
    }
}
