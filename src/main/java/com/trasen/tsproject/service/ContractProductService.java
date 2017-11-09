package com.trasen.tsproject.service;


import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
import com.trasen.tsproject.dao.TbHtModuleMapper;
import com.trasen.tsproject.dao.TbHtResolveMapper;
import com.trasen.tsproject.dao.TbProModulePriceMapper;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@Service
public class ContractProductService {
    private static final Logger logger = LoggerFactory.getLogger(ContractProductService.class);

    @Autowired
    private Environment env;

    /*
    * 合同模块
    * */
    @Autowired
    private TbHtModuleMapper tbHtModuleMapper;

    /*
    * 合同分解
    * */
    @Autowired
    private TbHtResolveMapper tbHtResolveMapper;

    /*
    * 标准价
    * */
    @Autowired
    private TbProModulePriceMapper tbProModulePriceMapper;

    @Autowired
    private TbHtHandoverMapper tbHtHandoverMapper;

    public Map<String,Object> getcontractTransenList(Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        String parameterJson = JSONObject.toJSONString(param);
        String contract_imis = env.getProperty("contract_imis");
        if(contract_imis==null){
            paramMap.put("success",false);
            paramMap.put("message","contract_imis参数错误");
            logger.info("合同列表查询失败，contract_imis参数错误");
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
                if(jsonObject.getString("contactPhone")!=null) contractInfo.setContactPhone(jsonObject.getString("contactPhone"));
                contractInfoList.add(contractInfo);
            }
            paramMap.put("list",contractInfoList);
            paramMap.put("totalPages",dataJson.getInteger("totalPages"));
            paramMap.put("pageNo",dataJson.getInteger("pageNo"));
            paramMap.put("totalCount",dataJson.getInteger("totalCount"));
            paramMap.put("pageSize",dataJson.getInteger("pageSize"));
            paramMap.put("success",true);
            logger.info("合同列表查询成功");
            return paramMap;
        }else{
            paramMap.put("success",false);
            paramMap.put("message","传入参数错误");
            logger.info("合同列表查询失败，传入参数错误");
            return paramMap;
        }
    }

    /*
    *
    * */
    public List<ContractInfo> getOaContractListByOwner(Map<String,String> param){
        String parameterJson = JSONObject.toJSONString(param);
        String contractOwen_imis = env.getProperty("contractOwen_imis");
        if(contractOwen_imis==null){
            logger.info("============"+"获取contractOwen_imis失败");
            return null;
        }
        String json= HttpUtil.connectURL(contractOwen_imis,parameterJson,"POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        if(dataJson.getBoolean("success")){
            String  jsonString=dataJson.getString("list");
            List<ContractInfo> list = JSON.parseArray(jsonString, ContractInfo.class);
            List<String> htNoList=tbHtHandoverMapper.queryHandOverByOwerOfType(param);
            if(Optional.ofNullable(list).isPresent()){
                List<ContractInfo> contractInfoList=list.stream().filter(contractInfo -> filterList(contractInfo.getContractNo(),param.get("status"),htNoList)).collect(Collectors.toList());
                return contractInfoList;
            }
            return list;
        }else{
            logger.info("============"+"查询数据失败");
            return null;
        }
    }

    public boolean filterList(String htNo,String status,List<String> htNoList){
        boolean boo=htNoList.contains(htNo);
        if(status.equals("1")){
            if(!boo){
                return true;
            }else{
                return false;
            }
        }else{
            if(boo){
                return true;
            }else{
                return false;
            }
        }
    }

    /*
    * 同步合同模块
    * */
    public boolean synchroHtModuleByContract(String contractNo,String hospitalLevel){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        String product_imis = env.getProperty("product_imis");
        if(product_imis==null){
            return false;
        }
        tbHtModuleMapper.deleteHtModule(contractNo);
        Map<String,String> jsonmap=new HashMap<String,String>();
        jsonmap.put("contractNo",contractNo);
        String parameterJson = JSONObject.toJSONString(jsonmap);
        String json= HttpUtil.connectURL(product_imis,parameterJson,"POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        if(dataJson.getBoolean("success")) {
            JSONArray jsonArray = dataJson.getJSONArray("list");
            List<TbHtModule> tbHtModuleList1=new ArrayList<TbHtModule>();
            for(java.util.Iterator tor=jsonArray.iterator();tor.hasNext();){
                TbHtModule htModule=new TbHtModule();
                TbProModulePrice tbProModulePrice=new TbProModulePrice();
                JSONObject jsonObject = (JSONObject)tor.next();
                htModule.setHtNo(jsonObject.getString("contractNo"));
                htModule.setModId(jsonObject.getString("productId"));
                htModule.setProCode(jsonObject.getString("type"));
                htModule.setCreated(new Date());

                tbProModulePrice.setModId(jsonObject.getString("productId"));
                tbProModulePrice.setHospitalLevel(hospitalLevel);
                tbProModulePrice=tbProModulePriceMapper.selectStandardPrice(tbProModulePrice);

                //设置标准价
                if(tbProModulePrice==null){
                    htModule.setPrice(1.0);
                }else{
                    htModule.setPrice(tbProModulePrice.getStandardPrice());
                }
                tbHtModuleMapper.saveHtModule(htModule);
            }
            logger.info("数据同步成功=======");
            return true;
        }else{
            return false;
        }

    }

    /*
    * 计算产值，小计和保存合同模块，合同分解
    * */
    @Transactional(rollbackFor=Exception.class)
    public boolean getOutputValueOrSubtotal(String htNo,double contractPrice){
        List<TbHtModule> tbHtModuleList=tbHtModuleMapper.selectModuleByHtNo(htNo);
        if(tbHtModuleList.size()==0){
            return false;
        }
        double standardPriceCount=0;
        //清空数据
        tbHtResolveMapper.deleteHtResolve(htNo);

        //组装分解数据
        Map<String,TbHtResolve> htResolveMap = new HashMap<>();
        for(TbHtModule tbHtModule:tbHtModuleList){
            standardPriceCount = standardPriceCount + tbHtModule.getPrice();
            if(htResolveMap.get(tbHtModule.getProCode())!=null){
                TbHtResolve htResolve = htResolveMap.get(tbHtModule.getProCode());
                double price = htResolve.getPrice();
                price = price+tbHtModule.getPrice();
                htResolve.setPrice(price);
            }else{
                TbHtResolve tbHtResolve = new TbHtResolve();
                tbHtResolve.setPrice(tbHtModule.getPrice());
                tbHtResolve.setHtNo(tbHtModule.getHtNo());
                tbHtResolve.setProCode(tbHtModule.getProCode());
                htResolveMap.put(tbHtModule.getProCode(),tbHtResolve);
            }
        }
        Collection<TbHtResolve> htResolveListT = htResolveMap.values();
        List<TbHtResolve> htResolveList = new ArrayList<>();
        htResolveList.addAll(htResolveListT);
        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.DOWN);
        //计算产值
        int outPutCount=0;
        for(TbHtResolve htResolve : htResolveList){
            //计算产品产值
            double price_output=htResolve.getPrice()/standardPriceCount;
            price_output=Double.valueOf(df.format(price_output));
            outPutCount=outPutCount+(int)(price_output*100);
            htResolve.setOutputValue((int)(price_output*100)+"%");
            double subtotal=price_output*contractPrice;
            subtotal=Double.valueOf(df.format(subtotal));

            htResolve.setSubtotal(subtotal);
            htResolve.setTotal(subtotal);
        }
        //平均分配余量
        if(htResolveList.size()>0&&100 - outPutCount>0&&100 - outPutCount<htResolveList.size()){
            for (int k = 0; k <100-outPutCount; k++) {
                String out=htResolveList.get(k).getOutputValue().substring(0, htResolveList.get(k).getOutputValue().length() - 1);
                int out_l = Double.valueOf(out).intValue() + 1;
                htResolveList.get(k).setOutputValue(out_l + "%");
                double subtotal_js = out_l * 0.01 * contractPrice;
                subtotal_js=Double.valueOf(df.format(subtotal_js));

                htResolveList.get(k).setSubtotal(subtotal_js);
                htResolveList.get(k).setTotal(subtotal_js);
            }
        }
        //保存产值
        for(TbHtResolve htResolve : htResolveList){
            tbHtResolveMapper.saveHtResolve(htResolve);
        }
        logger.info("产值和小计,保存更新成功=======");
        return true;
    }



     public List<TbHtResolve> queryHtResolve(String htNo){
         List<TbHtResolve> list = new ArrayList<>();
         if(!StringUtil.isEmpty(htNo)){
             list = tbHtResolveMapper.queryHtResolve(htNo);
         }
         return list;
     }

     public List<TbHtModule> queryHtModule(TbHtResolve htResolve){
         List<TbHtModule> list = new ArrayList<>();
         if(htResolve!=null){
             list = tbHtModuleMapper.queryHtModule(htResolve);
         }
         return list;
     }

     public boolean updateModulePrice(List<TbHtModule> list){
         boolean boo = false;
         if(list!=null){
             for(TbHtModule htModule : list){
                 htModule.setOperator(VisitInfoHolder.getUserId());
                 tbHtModuleMapper.updateModulePrice(htModule);
             }
             boo = true;
         }
         return boo;
     }

     public boolean updateResolveTotal(List<TbHtResolve> htResolveList){
         boolean boo = false;
         if(htResolveList!=null){
             for (TbHtResolve htResolve : htResolveList){
                 htResolve.setOperator(VisitInfoHolder.getUserId());
                 tbHtResolveMapper.updateResolveTotal(htResolve);
             }
             boo = true;
         }
         return boo;
     }



}
