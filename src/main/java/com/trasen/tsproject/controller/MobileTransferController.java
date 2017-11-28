package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TimeLineVo;
import com.trasen.tsproject.service.HandoverService;
import com.trasen.tsproject.service.TbMsgService;
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
 * @date 2017/11/28
 */
@RequestMapping(value="/mobileTransfer")
@RestController
public class MobileTransferController {
    private static final Logger logger = Logger.getLogger(MobileTransferController.class);

    @Autowired
    HandoverService handoverService;

    @Autowired
    TbMsgService tbMsgService;

    @RequestMapping(value="/queryHandOverList",method = RequestMethod.POST)
    public Result queryHandOverList(@RequestBody Map<String,String> param){
        Result result=new Result();
        try{
            param.put("htOwner",VisitInfoHolder.getShowName());
            List<TbHtHandover> tbHtHandoverList=handoverService.queryHandOverList(param);
            result.setObject(tbHtHandoverList);
            result.setSuccess(true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="queryHandOverByProcessId/{processId}",method = RequestMethod.POST)
    public Map<String,Object> queryHandOverByProcessId(@PathVariable String processId){
        Map<String,Object> result=new HashMap<>();
        try{
            Optional<String> op=Optional.ofNullable(processId);
            if(!op.isPresent()||op.get().equals("")){
                result.put("success",false);
                result.put("message","参数传入错误");
            }else{
                TbHtHandover tbHtHandover=tbMsgService.selectByProcessId(op.get());
                List<TimeLineVo> timeLineVos=handoverService.getTimeLine(op.get());
                result.put("tbHtHandover",tbHtHandover);
                result.put("timeLineVos",timeLineVos);
                result.put("success",true);
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }
}
