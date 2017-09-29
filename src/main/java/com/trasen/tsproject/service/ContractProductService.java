package com.trasen.tsproject.service;

import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtProductMapper;
import com.trasen.tsproject.dao.TbProModulePriceMapper;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.ContractProduct;
import com.trasen.tsproject.model.TbHtProduct;
import com.trasen.tsproject.model.TbProModulePrice;
import com.trasen.tsproject.model.TbStandardPrice;
import com.trasen.tsproject.util.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
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
    private static final Logger logger = LoggerFactory.getLogger(ContractProductService.class);

    @Autowired
    private Environment env;

    @Autowired
    private TbHtProductMapper tbHtProductMapper;

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

    public Map<String,Object> getProductByContract(String contractNo,String hospitalLevel,double contractPrice){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        String product_imis = env.getProperty("product_imis");
        if(product_imis==null){
            paramMap.put("success",false);
            paramMap.put("message","product_imis参数错误");
            logger.info("合同列表查询失败，product_imis参数错误");
            return paramMap;
        }
        if(contractNo!=null){
            List<TbHtProduct> tbHtProductList=tbHtProductMapper.selectTbHtProductByHtNo(contractNo);
            if(tbHtProductList.size()>0){
                paramMap.put("list",tbHtProductList);
                paramMap.put("success",true);
                return paramMap;
            }
            Map<String,String> jsonmap=new HashMap<String,String>();
            jsonmap.put("contractNo",contractNo);
            String parameterJson = JSONObject.toJSONString(jsonmap);
            String json= HttpUtil.connectURL(product_imis,parameterJson,"POST");
            JSONObject dataJson = (JSONObject) JSONObject.parse(json);
            if(dataJson.getBoolean("success")) {
                JSONArray jsonArray = dataJson.getJSONArray("list");
                List<TbHtProduct> tbHtProductList1=new ArrayList<TbHtProduct>();
                for(java.util.Iterator tor=jsonArray.iterator();tor.hasNext();){
                    TbProModulePrice tbProModulePrice=new TbProModulePrice();
                    TbHtProduct tbHtProduct=new TbHtProduct();
                    JSONObject jsonObject = (JSONObject)tor.next();
                    tbHtProduct.setHtNo(jsonObject.getString("contractNo"));
                    tbHtProduct.setProductId(String.valueOf(jsonObject.getInteger("productId")));
                    tbHtProduct.setProductName(jsonObject.getString("productName"));

                    //根据医院等级和产品列表查询标准价
                    tbProModulePrice.setHospitalLevel(hospitalLevel);
                    tbProModulePrice.setModId(String.valueOf(jsonObject.getInteger("productId")));
                    tbProModulePrice= tbProModulePriceMapper.selectStandardPrice(tbProModulePrice);

                    //计算产值和小计
                    if(tbProModulePrice==null){
                        tbHtProduct.setStandardPrice(1.0);
                    }else{
                        tbHtProduct.setStandardPrice(Double.valueOf(tbProModulePrice.getStandardPrice()));
                    }
                    //修改add
                    tbHtProductList1.add(tbHtProduct);
                }
                logger.info("根据合同查询产品列表成功=======");
                paramMap.put("list",tbHtProductList1);
                paramMap.put("success",true);
                return paramMap;
            }else{
                paramMap.put("message","根据合同查询产品列表失败");
                paramMap.put("success",false);
                logger.info("根据合同查询产品列表失败=======");
                return paramMap;
            }
        }else{
            paramMap.put("message","合同号为空");
            paramMap.put("success",false);
            logger.info("合同号为空，请检查传入参数=======");
            return paramMap;
        }
    }

    public Map<String,Object> updateProductByContract(String contractNo,int pkid,double contractPrice,double standardPrice){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        List<TbHtProduct> tbHtProductList=tbHtProductMapper.selectTbHtProductByHtNo(contractNo);
        for(int i=0;i<tbHtProductList.size();i++){
            if(pkid==tbHtProductList.get(i).getPkid()){
                tbHtProductList.get(i).setStandardPrice(standardPrice);
            }
        }
        List<TbHtProduct> tbHtProducts=getOutputValueOrSubtotal(tbHtProductList,contractPrice);
        logger.info("标准价更新成功=============");
        paramMap.put("list",tbHtProducts);
        paramMap.put("success",true);
        return paramMap;
    }

    /**
     * 计算产值和小计，保存，更新合同产品表
     */
    @Transactional(rollbackFor=Exception.class)
    public List<TbHtProduct> getOutputValueOrSubtotal(List<TbHtProduct> tbHtProductList,double contractPrice){
        double standardPriceCount=0;
        for(TbHtProduct tbHtProduct:tbHtProductList){
            standardPriceCount=standardPriceCount+Double.valueOf(tbHtProduct.getStandardPrice());
        }
        int outPutCount=0;

        for(TbHtProduct tbHtProduct:tbHtProductList){
                double output_value=Double.valueOf(tbHtProduct.getStandardPrice())/standardPriceCount;
                BigDecimal  bigDecimal=new BigDecimal(output_value);
                output_value=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                double subtotal=output_value*contractPrice;
                BigDecimal  bigDec=new BigDecimal(subtotal);
                subtotal=bigDec.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
                outPutCount=outPutCount+(int)(output_value*100);
                String  output_values=output_value*100+"%";
                tbHtProduct.setSubtotal(subtotal);
                tbHtProduct.setOutputValue(output_values);
                if(tbHtProduct.getPkid()!=null){
                    tbHtProductMapper.updateTbHtProduct(tbHtProduct);
                    logger.info("合同更新成功=======");
                }else{
                    tbHtProductMapper.saveTbHtProduct(tbHtProduct);
                    logger.info("合同保存成功=======");
                }

        }
        if(tbHtProductList.size()>0){
            if (100 - outPutCount != 0) {
                outPutCount = 100 - outPutCount;
                for (int i = 0; i < outPutCount; i++) {
                    String out = tbHtProductList.get(i).getOutputValue().substring(0, tbHtProductList.get(i).getOutputValue().length() - 1);
                    int out_l = Double.valueOf(out).intValue() + 1;
                    tbHtProductList.get(i).setOutputValue(out_l + "%");
                    double subtotal_js = out_l * 0.01 * contractPrice;
                    BigDecimal big_dou = new BigDecimal(subtotal_js);
                    subtotal_js = big_dou.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    tbHtProductList.get(i).setSubtotal(subtotal_js);
                    tbHtProductMapper.updateTbHtProduct(tbHtProductList.get(i));
                }
            }
        }
        logger.info("产值和小计,保存更新成功=======");
        return tbHtProductList;
    }
}
