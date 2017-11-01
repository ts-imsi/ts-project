package com.trasen.tsproject.controller;


import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbProjectManager;
import com.trasen.tsproject.service.ProjectArrangeService;
import org.mockito.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date 2017/10/30
 */
@RestController
@RequestMapping(value="/arrange")
public class ProjectArrangeController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectArrangeController.class);

    @Autowired
    private ProjectArrangeService projectArrangeService;

    @RequestMapping(value="/selectProjectArrangeList",method = RequestMethod.POST)
    public Map<String,Object> selectProjectArrangeList(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.isEmpty()||param.get("rows")==null||param.get("page")==null){
                result.put("success",false);
                result.put("message","参数为空");
            }
            Optional<String> opt=Optional.ofNullable(param.get("selectName"));
            TbHtHandover htHandover=new TbHtHandover();
            if(opt.isPresent()&&!opt.get().equals("")){
                htHandover.setCustomerName(opt.get());
                htHandover.setHtNo(opt.get());
                htHandover.setHtName(opt.get());
                htHandover.setProManager(opt.get());
            }
            Optional<String> opS=Optional.ofNullable(param.get("isArrange"));
            if(opS.isPresent()&&!opS.get().equals("")){
                htHandover.setIsArrange(Integer.valueOf(opS.get()));
            }
            PageInfo<TbHtHandover> pageInfo= projectArrangeService.selectProjectArrangeList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),htHandover);
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

    @RequestMapping(value="/getManageByType/{type}",method = RequestMethod.POST)
    public Result getManageByType(@PathVariable String type){
        Result result=new Result();
        try{
            List<TbProjectManager> projectManagerList=projectArrangeService.getManageByType(Optional.ofNullable(type).orElse("0"));
            result.setObject(projectManagerList);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/sendLetter",method = RequestMethod.POST)
    public Result sendLetter(@RequestBody Map<String,Object> param){
        Result result=new Result();
        try{
            if(param.isEmpty()){
                result.setSuccess(false);
                result.setMessage("参数传入错误");
            }else{
                TbHtHandover tbHtHandover=new TbHtHandover();
                Map<String,Object> handOverMap= (Map<String, Object>) param.get("handOver");
                tbHtHandover=JSON.parseObject(JSON.toJSONString(handOverMap), TbHtHandover.class);

                TbProjectManager tbProjectManager= new TbProjectManager();
                Map<String,Object> manageMap= (Map<String, Object>) param.get("projectManage");
                tbProjectManager=JSON.parseObject(JSON.toJSONString(manageMap), TbProjectManager.class);
                projectArrangeService.sendLetter(tbHtHandover,tbProjectManager);
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
}
