package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.Select;
import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.service.TbHtAnalyzeService;
import com.trasen.tsproject.service.TbPersonnelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
                Map<String,Object> object = tbHtAnalyzeService.selectAnalyzeList(htNo);
                result.setObject(object);
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
    public Result saveAnaly(@RequestBody List<Map<String,Object>> selectJson){
        Result result =new Result();
        try{
            if(selectJson==null||selectJson.size()==0){
                result.setSuccess(false);
                result.setMessage("参数错误");
            }else{
                List<TbHtAnalyze> htAnalyzes = new ArrayList<>();
                List<TbMsg> tbMsgs = new ArrayList<>();
                for(Map<String,Object> map : selectJson){
                    TbHtAnalyze analyze = new TbHtAnalyze();
                    TbMsg tbMsg=new TbMsg();


                    tbMsg.setSendName(VisitInfoHolder.getShowName());
                    tbMsg.setType("read");
                    tbMsg.setStatus(0);
                    tbMsg.setSendTime(new Date());


                    if(map.get("handoverId")!=null){
                        analyze.setHandoverId(map.get("handoverId").toString());
                    }
                    if(map.get("processId")!=null){
                        analyze.setProcessId(map.get("processId").toString());
                        tbMsg.setProcessId(map.get("processId").toString());
                    }
                    if(map.get("htNo")!=null){
                        analyze.setHtNo(map.get("htNo").toString());
                        tbMsg.setHtNo(map.get("htNo").toString());
                        tbMsg.setTitle(map.get("htNo").toString()+"合同生产确认");
                        tbMsg.setMsgContent("请确认合同["+map.get("htNo").toString()+"],如有出入请联系内控。");
                    }
                    if(map.get("id")!=null){
                        analyze.setOperator(map.get("id").toString());
                        tbMsg.setWorkNum(map.get("id").toString());
                    }
                    if(map.get("text")!=null){
                        tbMsg.setName(map.get("text").toString());
                    }
                    htAnalyzes.add(analyze);
                    tbMsgs.add(tbMsg);

                }
                tbHtAnalyzeService.saveAnaly(htAnalyzes,tbMsgs);
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


}
