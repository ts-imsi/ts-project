package com.transen.tsproject.service;

import com.transen.tsproject.model.ContractInfo;
import com.transen.tsproject.util.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@Service
public class ContractProductService {

    @Autowired
    private Environment env;

    public Map<String,Object> getcontractTransenList(Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        String parameterJson = JSONObject.toJSONString(param);

        String contract_imis = env.getProperty("contract_imis");
        if(contract_imis==null){
            paramMap.put("success",false);
            paramMap.put("message","contract_imis参数错误");
            return paramMap;
        }
        String json= HttpUtil.connectURL(contract_imis,parameterJson,"POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        if(dataJson.getBoolean("success")){
            JSONArray jsonArray=dataJson.getJSONArray("list");
            List<ContractInfo> contractInfoList=new ArrayList<ContractInfo>();
            for(java.util.Iterator tor=jsonArray.iterator();tor.hasNext();){
                ContractInfo contractInfo=new ContractInfo();
                JSONObject jsonObject = (JSONObject)tor.next();
                if(jsonObject.getInteger("id")!=null)contractInfo.setId(jsonObject.getInteger("id"));
                if(jsonObject.getString("contractNo")!=null)contractInfo.setContractNo(jsonObject.getString("contractNo"));
                if(jsonObject.getInteger("contractType")!=null)contractInfo.setContractType(jsonObject.getInteger("contractType"));
                if(jsonObject.getString("contractName")!=null) contractInfo.setContractName(jsonObject.getString("contractName"));
                if(jsonObject.getInteger("contractPrice")!=null)contractInfo.setContractPrice(jsonObject.getInteger("contractPrice"));
                if(jsonObject.getInteger("productType")!=null) contractInfo.setProductType(jsonObject.getInteger("productType"));
                if(jsonObject.getInteger("buyPrice")!=null)contractInfo.setBuyPrice(jsonObject.getInteger("buyPrice"));
                if(jsonObject.getDate("signDate")!=null) contractInfo.setSignDate(jsonObject.getDate("signDate"));
                if(jsonObject.getString("contractOwner")!=null) contractInfo.setContractOwner(jsonObject.getString("contractOwner"));
                if(jsonObject.getString("buyer")!=null) contractInfo.setBuyer(jsonObject.getString("buyer"));
                if(jsonObject.getString("seller")!=null) contractInfo.setSeller(jsonObject.getString("seller"));
                if(jsonObject.getString("buyerSigner")!=null) contractInfo.setBuyerSigner(jsonObject.getString("buyerSigner"));
                if(jsonObject.getString("sellerSigner")!=null) contractInfo.setSellerSigner(jsonObject.getString("sellerSigner"));
                if(jsonObject.getInteger("customerId")!=null) contractInfo.setCustomerId(jsonObject.getInteger("customerId"));
                if(jsonObject.getDate("createTime")!=null) contractInfo.setCreateTime(jsonObject.getDate("createTime"));
                if(jsonObject.getDate("timeLimit")!=null) contractInfo.setTimeLimit(jsonObject.getDate("timeLimit"));
                if(jsonObject.getInteger("state")!=null) contractInfo.setState(jsonObject.getInteger("state"));
                if(jsonObject.getString("paymode")!=null) contractInfo.setPaymode(jsonObject.getString("paymode"));
                if(jsonObject.getString("remark")!=null) contractInfo.setRemark(jsonObject.getString("remark"));
                if(jsonObject.getInteger("createType")!=null) contractInfo.setCreateType(jsonObject.getInteger("createType"));
                if(jsonObject.getString("createUserId")!=null) contractInfo.setCreateUserId(jsonObject.getString("createUserId"));
                if(jsonObject.getDate("distributeDate")!=null) contractInfo.setDistributeDate(jsonObject.getDate("distributeDate"));
                if(jsonObject.getDate("maintainLimit")!=null) contractInfo.setMaintainLimit(jsonObject.getDate("maintainLimit"));
                if(jsonObject.getString("customerName")!=null) contractInfo.setCustomerName(jsonObject.getString("customerName"));
                if(jsonObject.getString("customerNo")!=null) contractInfo.setCustomerNo(jsonObject.getString("customerNo"));
                if(jsonObject.getString("hospitalGrade")!=null) contractInfo.setHospitalGrade(jsonObject.getString("hospitalGrade"));
                if(jsonObject.getString("contactAddress")!=null) contractInfo.setContactAddress(jsonObject.getString("contactAddress"));
                contractInfoList.add(contractInfo);
            }
            paramMap.put("list",contractInfoList);
            paramMap.put("totalPages",dataJson.getInteger("totalPages"));
            paramMap.put("pageNo",dataJson.getInteger("pageNo"));
            paramMap.put("totalCount",dataJson.getInteger("totalCount"));
            paramMap.put("pageSize",dataJson.getInteger("pageSize"));
            paramMap.put("success",true);
            return paramMap;
        }else{
            paramMap.put("success",false);
            paramMap.put("message","传入参数错误");
            return paramMap;
        }
    }
}
