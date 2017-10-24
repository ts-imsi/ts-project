package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.service.HandoverService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/9/27.
 */
@RestController
@RequestMapping(value="/handover")
public class HandoverController {

    private static final Logger logger = Logger.getLogger(HandoverController.class);

    @Autowired
    HandoverService handoverService;

    /**
     * 获取交接单
     * */
    @RequestMapping(value="/getHandover", method = RequestMethod.POST)
    public Result getHandover(@RequestBody ContractInfo contractInfo)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(contractInfo!=null){
                TbHtHandover tbHtHandover = handoverService.getHandover(contractInfo);
                result.setSuccess(true);
                result.setObject(tbHtHandover);
            }
        }catch (Exception e) {
            logger.error("获取交接单异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取交接单失败");
        }
        return  result;
    }

    /**
     * 保存交接单
     * */
    @RequestMapping(value="/saveHandover", method = RequestMethod.POST)
    public Result saveHandover(@RequestBody TbHtHandover tbHtHandover)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(tbHtHandover!=null){
                boolean boo = handoverService.saveHandover(tbHtHandover);
                TbHtHandover handover = handoverService.getHandoverToHtNo(tbHtHandover.getHtNo());
                result.setSuccess(boo);
                result.setObject(handover);
            }
        }catch (Exception e) {
            logger.error("保存交接单异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存交接单失败");
        }
        return  result;
    }

    /**
     * 提交交接单
     * */
    @RequestMapping(value="/submitHandover", method = RequestMethod.POST)
    public Result submitHandover(@RequestBody TbHtHandover tbHtHandover)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(tbHtHandover!=null){
                boolean boo = handoverService.submitHandover(tbHtHandover);
                TbHtHandover handover = handoverService.getHandoverToHtNo(tbHtHandover.getHtNo());
                result.setSuccess(boo);
                result.setObject(handover);
            }
        }catch (Exception e) {
            logger.error("提交交接单异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("提交交接单失败");
        }
        return  result;
    }

    @RequestMapping(value="/getHtHandoverList",method = RequestMethod.POST)
    public Map<String,Object> getHtHandoverList(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        try {
            if(param.get("page")==null||param.get("rows")==null){
                paramMap.put("messages","参数错误");
                paramMap.put("success",false);
                return paramMap;
            }
            TbHtHandover tbHtHandover=new TbHtHandover();
            if(!StringUtil.isEmpty(param.get("selectName"))){
                tbHtHandover.setHtNo(param.get("selectName"));
                tbHtHandover.setHtName(param.get("selectName"));
                tbHtHandover.setCustomerName(param.get("selectName"));
            }
            if(!StringUtil.isEmpty(param.get("selectType"))){
                tbHtHandover.setType(param.get("selectType"));
            }
            PageInfo<TbHtHandover> tbHtHandoverPageInfo=handoverService.getHtHandoverList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbHtHandover);
            logger.info("数据查询条数"+tbHtHandoverPageInfo.getList().size());
            paramMap.put("totalPages",tbHtHandoverPageInfo.getPages());
            paramMap.put("pageNo",tbHtHandoverPageInfo.getPageNum());
            paramMap.put("totalCount",tbHtHandoverPageInfo.getTotal());
            paramMap.put("pageSize",tbHtHandoverPageInfo.getPageSize());
            paramMap.put("list",tbHtHandoverPageInfo.getList());
            paramMap.put("success",true);
            return paramMap;
        }catch (Exception e) {
            paramMap.put("messages","查询交接单错误");
            paramMap.put("success",false);
            return paramMap;
        }
    }

    @RequestMapping(value="/getHandover/{pkid}",method = RequestMethod.GET)
    public Result getTemplate(@PathVariable Integer pkid){
        //结果集
        Result result = new Result();
        result.setStatusCode(0);
        result.setSuccess(false);
        try {
            Map<String,Object> map = handoverService.getTempDataList(pkid);
            result.setObject(map);
            result.setSuccess(true);
            result.setStatusCode(1);
        } catch (IllegalArgumentException e) {
            logger.error("获取交接单模板数据异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取交接单模板数据异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/timeLine/{processId}",method = RequestMethod.GET)
    public Result getTimeLine(@PathVariable String processId){
        //结果集
        Result result = new Result();
        result.setStatusCode(0);
        result.setSuccess(false);
        try {
            List<TimeLineVo> list = handoverService.getTimeLine(processId);
            result.setSuccess(true);
            result.setStatusCode(1);
            result.setObject(list);
        } catch (IllegalArgumentException e) {
            logger.error("获取交接单进度数据异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取交接单进度数据异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }



}
