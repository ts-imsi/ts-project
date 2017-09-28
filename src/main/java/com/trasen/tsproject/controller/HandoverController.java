package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbTemplate;
import com.trasen.tsproject.service.HandoverService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
                result.setSuccess(boo);
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





}
