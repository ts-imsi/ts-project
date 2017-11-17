package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbPlanTemplate;
import com.trasen.tsproject.model.TwfStage;
import com.trasen.tsproject.model.TwfStageDoc;
import com.trasen.tsproject.service.PlanTemplateService;
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

    @RequestMapping(value="/selectTwfStageTag",method = RequestMethod.POST)
    public Map<String,Object> selectTwfStageTag(){
        Map<String,Object> result=new HashMap<>();
        try{
            result=planTemplateService.selectTwfStageTag();
            result.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }

    @RequestMapping(value="/getTwfStageDocList/{stageId}",method = RequestMethod.POST)
    public Result getTwfStageDocList(@PathVariable Integer stageId){
        Result result=new Result();
        try{
            if(Optional.ofNullable(stageId).isPresent()&&stageId!=0){
                List<TwfStageDoc> twfStageDocList=planTemplateService.getTwfStageDocList(stageId);
                result.setSuccess(true);
                result.setObject(twfStageDocList);
            }else{
                result.setSuccess(false);
                result.setMessage("参数传入错误");
            }

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        return result;
    }

    @RequestMapping(value="/savePlanTemp",method = RequestMethod.POST)
    public Result savePlanTemp(@RequestBody Map<String,Object> param){
        Result result=new Result();
        try{
            List<String> tagSaveList=(List<String>)param.get("tagSaveList");
            List<String> stageSaveList=(List<String>)param.get("stageSaveList");

            Optional tagOp=Optional.ofNullable(tagSaveList);
            Optional stageOp=Optional.ofNullable(stageSaveList);

            if(!tagOp.isPresent()||!stageOp.isPresent()){
                result.setSuccess(false);
                result.setMessage("参数传入错误");
            }else{
                TbPlanTemplate tbPlanTemplate=new TbPlanTemplate();
                Map<String,Object> tbPlanTemplateMap= (Map<String, Object>) param.get("tbPlanTemplate");
                tbPlanTemplate= JSON.parseObject(JSON.toJSONString(tbPlanTemplateMap), TbPlanTemplate.class);
                planTemplateService.savePlanTemp(tbPlanTemplate,tagSaveList,stageSaveList);
                result.setSuccess(true);
                result.setMessage("数据保存成功");
            }

        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }

    @RequestMapping(value="/saveStageTemp",method = RequestMethod.POST)
    public Result saveStageTemp(@RequestBody TwfStage twfStage){
        Result result=new Result();
        try{
            if(Optional.ofNullable(twfStage).isPresent()){
                planTemplateService.saveStageTemp(twfStage);
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

    @RequestMapping(value="/saveTwfStageDoc",method = RequestMethod.POST)
    public Result saveTwfStageDoc(@RequestBody TwfStageDoc twfStageDoc){
        Result result=new Result();
        try{
            if(Optional.ofNullable(twfStageDoc).isPresent()){
                planTemplateService.saveTwfStageDoc(twfStageDoc);
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

    @RequestMapping(value="/deleteStageTemp/{pkid}",method = RequestMethod.POST)
    public Result deleteStageTemp(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            planTemplateService.deleteStageTemp(Optional.ofNullable(pkid).orElse(0));
            result.setSuccess(true);
            result.setMessage("数据删除成功");
        }catch (Exception e){
            logger.error("数据删除失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据删除失败");
        }
        return result;
    }
    @RequestMapping(value="/deleteDocByPkid/{pkid}",method = RequestMethod.POST)
    public Result deleteDocByPkid(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            planTemplateService.deleteDocByPkid(Optional.ofNullable(pkid).orElse(0));
            result.setSuccess(true);
            result.setMessage("数据删除成功");
        }catch (Exception e){
            logger.error("数据删除失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据删除失败");
        }
        return result;
    }

    @RequestMapping(value="/selectTempView/{pkid}",method = RequestMethod.POST)
    public Map<String,Object> selectTempView(@PathVariable Integer pkid){
        Map<String,Object> result=new HashMap<>();
        try{
            Map<String,Object> param=planTemplateService.selectTempView(Optional.ofNullable(pkid).orElse(0));
            param.put("success",true);
            result=param;
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }

}
