package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.model.TbProjectPlanLog;
import com.trasen.tsproject.service.ProjectPlanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    @RequestMapping(value="/queryProjectPlanList/{handoverId}",method = RequestMethod.POST)
    public Result queryProjectPlanList(@PathVariable String handoverId){
        Result result=new Result();
        try{
            List<TbProjectPlan> projectPlanList=projectPlanService.queryProjectPlanList(Optional.ofNullable(handoverId).orElse("0"));
            result.setSuccess(true);
            result.setObject(projectPlanList);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        return result;
    }


    @RequestMapping(value="/selectProjecActualizetList",method = RequestMethod.POST)
    public Map<String,Object> selectProjecActualizetList(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.isEmpty()||param.get("rows")==null||param.get("page")==null){
                result.put("success",false);
                result.put("message","参数为空");
            }
            Optional<String> opt=Optional.ofNullable(param.get("selectName"));
            TbHtHandover tbhtHandover=new TbHtHandover();
            if(opt.isPresent()&&!opt.get().equals("")){
                tbhtHandover.setCustomerName(opt.get());
                tbhtHandover.setHtNo(opt.get());
                tbhtHandover.setHtName(opt.get());
                tbhtHandover.setProManager(opt.get());
            }
            Optional<String> opS=Optional.ofNullable(param.get("isArrange"));
            if(opS.isPresent()&&!opS.get().equals("")){
                tbhtHandover.setIsArrange(Integer.valueOf(opS.get()));
            }
            PageInfo<TbHtHandover> pageInfo=projectPlanService.selectProjecActualizetList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbhtHandover);
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


    @RequestMapping(value="/saveProjectActualizePlan",method = RequestMethod.POST)
    public Result saveProjectActualizePlan(@RequestBody List<TbProjectPlan> tbProjectPlanList){
        Result result=new Result();
        try{
            if(!Optional.ofNullable(tbProjectPlanList).isPresent()){
                result.setMessage("传入参数错误");
                result.setSuccess(false);
            }else{
                projectPlanService.saveProjectActualizePlan(tbProjectPlanList);
                result.setMessage("数据保存成功");
                result.setSuccess(true);
            }
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }



}
