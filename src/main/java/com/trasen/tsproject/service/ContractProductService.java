package com.trasen.tsproject.service;


import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.dao.TbHtModuleMapper;
import com.trasen.tsproject.dao.TbHtResolveMapper;
import com.trasen.tsproject.dao.TbProModulePriceMapper;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.model.TbHtResolve;
import com.trasen.tsproject.model.TbProModulePrice;
import com.trasen.tsproject.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

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
    * 同步合同模块
    * */
    public Map<String,Object> synchroHtModuleByContract(String contractNo,String hospitalLevel,double contractPrice){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        String product_imis = env.getProperty("product_imis");
        if(product_imis==null){
            paramMap.put("success",false);
            paramMap.put("message","product_imis参数错误");
            logger.info("合同列表查询失败，product_imis参数错误");
            return paramMap;
        }
        List<TbHtModule> tbHtModuleList= tbHtModuleMapper.selectModuleByHtNo(contractNo);
        if(tbHtModuleList.size()>0){
            paramMap.put("success",false);
            paramMap.put("message","数据已存在不需要同步");
            logger.info("数据已存在不需要同步=======");
            return paramMap;
        }
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
                tbHtModuleList1.add(htModule);
            }
            boolean boo=getOutputValueOrSubtotal(tbHtModuleList1,contractNo,contractPrice);
            logger.info("数据同步成功=======");
            paramMap.put("messge","数据同步成功");
            paramMap.put("success",true);
            return paramMap;
        }else{
            logger.info("查询数据失败=======");
            paramMap.put("messge","查询数据失败");
            paramMap.put("success",false);
            return paramMap;
        }

    }

    /*
    * 计算产值，小计和保存合同模块，合同分解
    * */
    @Transactional(rollbackFor=Exception.class)
    public boolean getOutputValueOrSubtotal(List<TbHtModule> tbHtModuleList,String htNo,double contractPrice){
        double standardPriceCount=0;
        tbHtModuleMapper.deleteHtModule(htNo);
        tbHtResolveMapper.deleteHtResolve(htNo);
        Map<String, String> map = new HashMap<String, String>();
        for(TbHtModule tbHtModule:tbHtModuleList){
            standardPriceCount=standardPriceCount+Double.valueOf(tbHtModule.getPrice());
            map.put(tbHtModule.getProCode(),tbHtModule.getProCode());
            tbHtModuleMapper.saveHtModule(tbHtModule);
        }
        Collection<String> valueCollection = map.values();
        List<String> procodeList = new ArrayList<String>(valueCollection);
        List<TbHtResolve> tbHtResolveList=new ArrayList<TbHtResolve>();
        int outPutCount=0;
        for(int i=0;i<procodeList.size();i++){
            TbHtResolve tbHtResolve=new TbHtResolve();
            double pro_doc=0;
            tbHtResolve.setHtNo(htNo);
            for(int j=0;j<tbHtModuleList.size();j++){
                if(procodeList.get(i).equals(tbHtModuleList.get(j).getProCode())){
                    pro_doc=pro_doc+tbHtModuleList.get(j).getPrice();
                }
            }
            //计算产品产值
            double price_output=pro_doc/standardPriceCount;
            BigDecimal bigDecimal=new BigDecimal(price_output);
            price_output=bigDecimal.setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
            outPutCount=outPutCount+(int)(price_output*100);
            logger.info("======="+price_output);
            //计算产品小计
            double subtotal=price_output*contractPrice;

            tbHtResolve.setPrice(pro_doc);
            tbHtResolve.setProCode(procodeList.get(i));
            tbHtResolve.setSubtotal(subtotal);
            tbHtResolve.setCreated(new Date());
            logger.info("==========="+(int)(price_output*100)+"%");
            tbHtResolve.setOutputValue((int)(price_output*100)+"%");
            tbHtResolveMapper.saveHtResolve(tbHtResolve);
            tbHtResolveList.add(tbHtResolve);
        }

        if(tbHtResolveList.size()>0&&100 - outPutCount!=0){
            for (int k = 0; k <100-outPutCount; k++) {
                String out=tbHtResolveList.get(k).getOutputValue().substring(0, tbHtResolveList.get(k).getOutputValue().length() - 1);
                int out_l = Double.valueOf(out).intValue() + 1;
                tbHtResolveList.get(k).setOutputValue(out_l + "%");
                double subtotal_js = out_l * 0.01 * contractPrice;
                tbHtResolveList.get(k).setSubtotal(subtotal_js);
                tbHtResolveMapper.updateHtResolve(tbHtResolveList.get(k));
            }
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
                 tbHtModuleMapper.updateModulePrice(htModule);
             }
             boo = true;
         }
         return boo;
     }

}
