package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.service.TbMsgService;
import org.apache.commons.collections.MapUtils;
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
 * @date 2017/10/13
 */
@RestController
@RequestMapping(value="/tb_message")
public class MessageController {

    private static final Logger logger = Logger.getLogger(MessageController.class);

    @Autowired
    private TbMsgService tbMsgService;

    @RequestMapping(value="/selectTbMsg",method = RequestMethod.POST)
    public Map<String,Object> selectTbMsg(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.get("page")==null||param.get("rows")==null){
                result.put("messages","参数错误");
                result.put("success",false);
                return result;
            }
            PageInfo<TbMsg> tbMsgPageInfo=tbMsgService.selectTbMsg(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),param);
            logger.info("数据查询条数"+tbMsgPageInfo.getList().size());
            result.put("totalPages",tbMsgPageInfo.getPages());
            result.put("pageNo",tbMsgPageInfo.getPageNum());
            result.put("totalCount",tbMsgPageInfo.getTotal());
            result.put("pageSize",tbMsgPageInfo.getPageSize());
            result.put("list",tbMsgPageInfo.getList());
            result.put("success",true);
        }catch(Exception e){
            logger.error("信息查询失败=="+e.getMessage(),e);
            result.put("success",false);
            result.put("message","查询数据失败");
        }
        return result;
    }

    @RequestMapping(value="/getTbMsgById/{pkid}",method = RequestMethod.GET)
    public Result getTbMsgById(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            if(pkid==null){
                result.setSuccess(false);
                result.setMessage("参数错误");
                return result;
            }
            TbMsg tbMsg=tbMsgService.getTbMsgById(pkid);
            result.setSuccess(true);
            result.setObject(tbMsg);
        }catch (Exception e){
            logger.error("getTbMsgById查询失败=="+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("查询数据失败");
        }
        return result;
    }

    @RequestMapping(value="/submitFlow",method = RequestMethod.POST)
    public Result submitFlow(@RequestBody TbMsg tbMsg){
        Result result=new Result();
        try{
            if(tbMsg==null){
                result.setMessage("参数错误");
                result.setSuccess(false);
            }else{
                boolean boo=tbMsgService.submitFlow(tbMsg);
                if(boo){
                    result.setMessage("流程提交成功");
                    result.setSuccess(true);
                }else{
                    result.setMessage("流程提交失败");
                    result.setSuccess(true);
                }
            }
        }catch (Exception e){
            logger.error("流程提交错误"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("流程提交错误");
        }
        return result;
    }
    @RequestMapping(value="/returnFlow",method = RequestMethod.POST)
    public Result returnFlow(@RequestBody TbMsg tbMsg){
        Result result=new Result();
        try{
            if(tbMsg==null){
                result.setMessage("参数错误");
                result.setSuccess(false);
            }else{
                boolean boo=tbMsgService.returnFlow(tbMsg);
                if(boo){
                    result.setMessage("流程驳回成功");
                    result.setSuccess(true);
                }else{
                    result.setMessage("流程驳回成功");
                    result.setSuccess(true);
                }
            }
        }catch (Exception e){
            logger.error("流程驳回错误"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("流程驳回错误");
        }
        return result;
    }
}
