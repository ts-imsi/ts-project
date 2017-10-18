package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.model.Select;
import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.service.TbHtAnalyzeService;
import com.trasen.tsproject.service.TbPersonnelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/17
 */
@RestController
@RequestMapping(value="/ht_analyze")
public class TbHtAnalyzeController {

    private static final Logger logger = Logger.getLogger(TbHtAnalyzeController.class);

    @Autowired
    private TbHtAnalyzeService tbHtAnalyzeService;

    @Autowired
    private TbPersonnelService tbPersonnelService;

    @RequestMapping(value="/selectAnalyzeList/{htNo}",method = RequestMethod.GET)
    public Result selectAnalyzeList(@PathVariable String htNo){
        Result result=new Result();
        try{
            if(StringUtils.isEmpty(htNo)){
                result.setSuccess(false);
                result.setMessage("参数错误");
            }else{
                List<TbHtAnalyze> tbHtAnalyzeList=tbHtAnalyzeService.selectAnalyzeList(htNo);
                result.setObject(tbHtAnalyzeList);
                result.setSuccess(true);
            }
        }catch (Exception e){
            logger.error("查询数据失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("查询数据失败");
        }
        return result;
    }

    @RequestMapping(value="/saveAnaly",method = RequestMethod.POST)
    public Result saveAnaly(@RequestBody List<TbHtAnalyze> htAnalyzes){
        Result result =new Result();
        try{
            if(htAnalyzes==null||htAnalyzes.size()==0){
                result.setSuccess(false);
                result.setMessage("参数错误");
            }else{
                tbHtAnalyzeService.saveAnaly(htAnalyzes);
                result.setSuccess(true);
                result.setMessage("数据保存成功");
            }
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setMessage("数据保存失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/selectTbPersonnelList/{depId}",method = RequestMethod.GET)
    public Map<String,Object> selectTbPersonnelList(@PathVariable String depId){
        Map<String,Object>  result=new HashMap<>();
        try{
            if(StringUtils.isEmpty(depId)){
                result.put("success",false);
                result.put("message","参数错误");
            }else {
                List<Select> tbPersonnelList=tbPersonnelService.selectTbPersonnel(depId);
                result.put("data",tbPersonnelList);
                result.put("success",true);
            }
        }catch (Exception e){
            logger.error("查询数据失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","查询数据失败");
        }
        return result;
    }
}
