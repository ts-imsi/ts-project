package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TbPlanTemplate;
import com.trasen.tsproject.model.TwfStage;
import com.trasen.tsproject.service.PlanTemplateService;
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
 * @date 2017/11/15
 */
@RestController
@RequestMapping(value="/planTemplate")
public class PlanTemplateController {

    private static final Logger logger = Logger.getLogger(PlanTemplateController.class);

    @Autowired
    PlanTemplateService planTemplateService;

    @RequestMapping(value="/queryPlanTemp",method = RequestMethod.POST)
    public Map<String,Object> queryPlanTemp(@RequestBody Map<String,String> param){
        Map<String,Object> mapParam=new HashMap<>();
        try{
            if(param.isEmpty()){
                mapParam.put("message","传入参数失败");
                mapParam.put("success",false);
            }

            PageInfo<TbPlanTemplate> templatePageInfo= planTemplateService.queryPlanTemp(Integer.valueOf(Optional.ofNullable(param.get("page")).orElse("1")),Integer.valueOf(Optional.ofNullable(param.get("rows")).orElse("10")));
            logger.info("数据查询条数"+templatePageInfo.getList().size());
            mapParam.put("totalPages",templatePageInfo.getPages());
            mapParam.put("pageNo",templatePageInfo.getPageNum());
            mapParam.put("totalCount",templatePageInfo.getTotal());
            mapParam.put("pageSize",templatePageInfo.getPageSize());
            mapParam.put("list",templatePageInfo.getList());
            mapParam.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            mapParam.put("message","数据查询失败");
            mapParam.put("success",false);
        }
        return mapParam;
    }

    @RequestMapping(value="/selectTwfStageList",method = RequestMethod.POST)
    public Result selectTwfStageList(){
        Result result=new Result();
        try{
            List<TwfStage> twfStageList=planTemplateService.selectTwfStageList();
            result.setSuccess(true);
            result.setObject(twfStageList);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        return result;
    }
}
