package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.service.TbMsgService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
 * @date 2017/11/23
 */
@RestController
@RequestMapping(value="/mobileTransact")
public class MobileTransactController {
    private static final Logger logger = Logger.getLogger(MobileTransactController.class);

    @Autowired
    TbMsgService tbMsgService;

    @RequestMapping(value="/queryMobileTransactList/{status}",method = RequestMethod.POST)
    public Result queryMobileTransactList(@PathVariable Integer status){
        Result result=new Result();
        try{
            if(Optional.ofNullable(status).isPresent()){
                Map<String,Object> param=new HashMap<>();
                param.put("userId", VisitInfoHolder.getUserId());
                param.put("status",status);
                List<TbMsg> tbMsgList=tbMsgService.queryMobileTransactList(param);
                result.setObject(tbMsgList);
                result.setSuccess(true);
            }else{
                result.setMessage("参数传入错误");
                result.setSuccess(false);
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        return  result;
    }

    @RequestMapping(value="/queryMobileTreatedByPkid/{pkid}",method = RequestMethod.POST)
    public Map<String,Object> queryMobileTreatedByPkid(@PathVariable Integer pkid){
        Map<String,Object> param=new HashMap<>();
        try{
            if(Optional.ofNullable(pkid).isPresent()){
                param=tbMsgService.queryMobileTreatedByPkid(pkid);
                param.put("success",true);
            }else{
                param.put("success",false);
                param.put("message","参数传入错误");
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            param.put("success",false);
            param.put("message","数据查询失败");
        }
        return param;
    }
}
