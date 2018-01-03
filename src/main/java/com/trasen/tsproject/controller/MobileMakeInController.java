package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbTemplateItem;
import com.trasen.tsproject.service.ContractProductService;
import com.trasen.tsproject.service.HandoverService;
import com.trasen.tsproject.util.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @date 2018/1/3
 */
@RestController
@RequestMapping(value="/mobileMakeIn")
public class MobileMakeInController {

    private static final Logger logger = Logger.getLogger(MobileMakeInController.class);

    @Autowired
    ContractProductService contractProductService;

    @Autowired
    HandoverService handoverService;

    @RequestMapping(value="/getOaContractListByOwner")
    public Map<String,Object> getOaContractListByOwner(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.get("showAll")==null||!"all".equals(param.get("showAll"))){
                //列表权限
                param.put("contractOwner", VisitInfoHolder.getShowName());
            }
            param.put("status","1");
            List<ContractInfo> contractInfosD=contractProductService.getOaContractListByOwner(param);
            param.put("status","2");
            List<ContractInfo> contractInfosM=contractProductService.getOaContractListByOwner(param);
            result.put("contractInfosD",contractInfosD);
            result.put("contractInfosM",contractInfosM);
            result.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }

    @RequestMapping(value="/getMobileHandover", method = RequestMethod.POST)
    public Result getMobileHandover(@RequestBody ContractInfo conInfo)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(conInfo!=null){
                TbHtHandover tbHtHandover = handoverService.getHandover(conInfo);
                result.setObject(tbHtHandover);
                result.setSuccess(true);
            }
        }catch (Exception e) {
            logger.error("获取交接单异常" + e.getMessage(), e);
            result.setMessage("获取交接单失败");
            result.setSuccess(false);
        }
        return  result;
    }

    @RequestMapping(value="/saveMobileHandover", method = RequestMethod.POST)
    public Result saveMobileHandover(@RequestBody TbHtHandover TbHandover)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(TbHandover!=null){
                boolean boo = handoverService.saveHandover(TbHandover);
                TbHtHandover handover = handoverService.getHandoverToHtNo(TbHandover);
                result.setObject(handover);
                result.setSuccess(boo);
            }
        }catch (Exception e) {
            logger.error("保存交接单异常" + e.getMessage(), e);
            result.setMessage("保存交接单失败");
            result.setSuccess(false);
        }
        return  result;
    }

    /**
     * 提交交接单
     * */
    @RequestMapping(value="/submitMobileHandover", method = RequestMethod.POST)
    public Result submitMobileHandover(@RequestBody TbHtHandover tbHandover)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(tbHandover!=null){
                if(tbHandover.getPkid()==null){
                    handoverService.saveHandover(tbHandover);
                }
                List<TbTemplateItem> tbTemplateItems=tbHandover.getContentJson();
                tbTemplateItems.stream().forEach(tbTemplateItem -> addContentJson(tbTemplateItem));
                if(Optional.ofNullable(tbHandover.getContentJson()).isPresent()){
                    String content = JSON.toJSONString(tbHandover.getContentJson());
                    tbHandover.setContent(content);
                }
                boolean boo = handoverService.submitHandover(tbHandover);
                TbHtHandover handover = handoverService.getHandoverToHtNo(tbHandover);
                result.setObject(handover);
                result.setSuccess(boo);

            }
        }catch (Exception e) {
            logger.error("提交交接单异常" + e.getMessage(), e);
            result.setMessage("提交交接单失败");
            result.setSuccess(false);
        }
        return  result;
    }

    public void addContentJson(TbTemplateItem tbTemplateItem){
        if(tbTemplateItem.getCode().equals("saleSign")){
            tbTemplateItem.setModule(VisitInfoHolder.getShowName());
            tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
        }
    }

}
