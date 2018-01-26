package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.controller.IndexController;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.model.TimeLineVo;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/22
 */
@Service
public class IndexService {

    private final  static Logger logger=Logger.getLogger(IndexService.class);

    @Autowired
    TbMsgMapper tbMsgMapper;

    @Autowired
    TbHtHandoverMapper tbHtHandoverMapper;

    @Autowired
    HandoverService handoverService;

    @Autowired
    private Environment env;

    @Autowired
    ContractProductService contractProductService;

    public Map<String,Object> getIndexMain(){
        Map<String,Object> param=new HashMap<>();
        TbMsg tbMsg=tbMsgMapper.getIndexMsgByDate(VisitInfoHolder.getShowName());
        if(Optional.ofNullable(tbMsg).isPresent()){
            List<TimeLineVo> tiemLine=handoverService.getTimeLine(Optional.ofNullable(tbMsg.getProcessId()).orElse("0"));
            param.put("tiemLine",tiemLine);
        }
        String authorize_subordinate=env.getProperty("authorize_subordinate");
        if(authorize_subordinate!=null){
            authorize_subordinate=authorize_subordinate.replace("{appId}","ts-imis").replace("{userId}",VisitInfoHolder.getUserId());
        }
        Long signIn=new Long(0);
        Long sign=new Long(0);
        String result = HttpUtil.connectURLGET(authorize_subordinate);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        if(jsonObject.getJSONObject("object")!=null){
            List<TbPersonnel> list=JSON.parseArray(jsonObject.getString("object"),TbPersonnel.class);
            signIn=list.stream().filter(n->n.getSigninType().equals("signIn")).count();
            sign=list.stream().filter(n->n.getSigninType().equals("sign")).count();
        }else{
            signIn=new Long(1);
            sign=new Long(1);
        }
        param.put("signIn",signIn);
        param.put("sign",sign);
        return param;
    }

    public Map<String,Object> getHandoverOrPlan(){
        Map<String,Object> result=new HashMap<>();
        Map<String,String> param=new HashMap<>();
        param.put("contractOwner",VisitInfoHolder.getShowName());
        param.put("status","1");
        List<ContractInfo> contractInfoList=contractProductService.getOaContractListByOwner(param);
        if(contractInfoList!=null){
            result.put("conInfoSize",contractInfoList.size());
        }else{
            result.put("conInfoSize",0);
        }
        Integer count=tbHtHandoverMapper.getJxProNum(VisitInfoHolder.getShowName());
        if(count==null){
            count=0;
        }
        result.put("jxPro",count);
        return result;
    }
}
