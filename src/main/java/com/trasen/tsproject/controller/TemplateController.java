package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbTemplate;
import com.trasen.tsproject.model.TbTemplateItem;
import com.trasen.tsproject.service.TemplateService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangxiahui on 17/9/26.
 */
@RestController
@RequestMapping(value="/template")
public class TemplateController {

    private static final Logger logger = Logger.getLogger(TemplateController.class);

    @Autowired
    TemplateService templateService;


    @RequestMapping(value="/queryItem/{type}",method = RequestMethod.GET)
    public Result queryItem(@PathVariable String type){
        //结果集
        Result result = new Result();
        result.setStatusCode(0);
        result.setSuccess(false);
        try {
            List<TbTemplateItem> list = templateService.queryItemList(type);
            result.setObject(list);
            result.setSuccess(true);
            result.setStatusCode(1);
        } catch (IllegalArgumentException e) {
            logger.error("获取模版元素列表异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取模板元素列表异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/getTemplate/{tid}",method = RequestMethod.GET)
    public Result getTemplate(@PathVariable Integer tid){
        //结果集
        Result result = new Result();
        result.setStatusCode(0);
        result.setSuccess(false);
        try {
            TbTemplate tbTemplate = templateService.getTemplate(tid);
            result.setObject(tbTemplate);
            result.setSuccess(true);
            result.setStatusCode(1);
        } catch (IllegalArgumentException e) {
            logger.error("获取模板异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("获取模板列表异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 修改级别
     * */
    @RequestMapping(value="/saveTemplate", method = RequestMethod.POST)
    public Result saveTemplate(@RequestBody TbTemplate tbTemplate)  {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("保存失败");
        try {
            //数据更新
            if(tbTemplate!=null){
                result.setSuccess(true);
                result.setMessage("保存成功");






            }
        }catch (Exception e) {
            logger.error("保存异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("保存失败");
        }
        return  result;

    }


}
