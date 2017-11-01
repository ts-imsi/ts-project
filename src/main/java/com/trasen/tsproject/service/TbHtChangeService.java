package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.*;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/12
 */
@Service
public class TbHtChangeService {

    private static final Logger logger = LoggerFactory.getLogger(TbHtChangeService.class);

    @Autowired
    private TbHtChangeMapper tbHtChangeMapper;

    @Autowired
    private TbProModuleMapper tbProModuleMapper;

    @Autowired
    private TbProModulePriceMapper tbProModulePriceMapper;

    @Autowired
    private TbHtModuleMapper tbHtModuleMapper;

    @Autowired
    private TbHtModuleChangeMapper tbHtModuleChangeMapper;

    @Autowired
    private ContractProductService contractProductService;

    @Autowired
    private Environment env;

    public PageInfo<TbHtChange> getHtChangeList(int page,int rows,TbHtChange tbHtChange){
        PageHelper.startPage(page,rows);
        List<TbHtChange> tbHtChangeList=tbHtChangeMapper.getHtChangeList(tbHtChange);
        PageInfo<TbHtChange> pagehelper = new PageInfo<TbHtChange>(tbHtChangeList);
        return pagehelper;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean applySubmit(TbHtChange tbHtChange,List<String> oldModuleList,List<String> newModuleList,String hosLevel,String price){
        boolean boo=false;
        tbHtChangeMapper.saveHtChange(tbHtChange);
        //拼接合同编号
        String changeNo = tbHtChange.getType()+"-"+tbHtChange.getPkid();
        logger.info("合同变更，合同变更id"+tbHtChange.getPkid());
        //String htType,String module,String moduleType,Integer pkid,String hosLevel
        if(Optional.ofNullable(oldModuleList).isPresent()){
            oldModuleList.stream().forEach(old->saveHtChange(changeNo,old,"old",hosLevel));
        }
        if(Optional.ofNullable(newModuleList).isPresent()){
            newModuleList.stream().forEach(newm->saveHtChange(changeNo,newm,"new",hosLevel));
        }
        contractProductService.getOutputValueOrSubtotal(changeNo,Double.valueOf(price));
        //TODO 启动流程
        String process_start=env.getProperty("process_start").replace("{key}","addChange");
        if(process_start==null){
            logger.info("交接单提交获取process_start失败");
            return false;
        }
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("htNo",tbHtChange.getHtNo());
        param.put("htOwner",tbHtChange.getHtOwner());

        String parameterJson = JSONObject.toJSONString(param);
        String json= HttpUtil.connectURL(process_start,parameterJson,"POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        String process_id=null;
        if(dataJson.getInteger("code")==1){
            JSONObject jsonObject=dataJson.getJSONObject("processInstance");
            process_id=jsonObject.getString("id");
        }else{
            logger.info("交接单流程启动失败");
            return false;
        }
        tbHtChange.setStatus(0);//待审批
        tbHtChange.setProcessId(process_id);
        tbHtChangeMapper.updateHtChange(tbHtChange);
        boo=true;
        return boo;
    }

    public Map<String,Object> getContractByHtNo(String contractNo){
        Map<String,Object> param=new HashMap<>();
        String contract_htNo=env.getProperty("contract_htNo").replace("{contractNo}",contractNo);
        Optional<String> op=Optional.ofNullable(contract_htNo);
        if(!op.isPresent()){
            param.put("message","获取contract_htNo失败");
            param.put("success",false);
        }else{
            String json= HttpUtil.connectURL(op.get(),"","POST");
            JSONObject dataJson = (JSONObject) JSONObject.parse(json);
            if(dataJson.getBoolean("success")){
                JSONObject jsonObject=dataJson.getJSONObject("object");
                ContractInfo contractInfo=new ContractInfo();
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

                param.put("object",contractInfo);
                param.put("success",true);
            }else{
                logger.error("数据查询单个合同失败");
                param.put("message","数据查询单个合同失败");
                param.put("success",false);
            }
        }
        return param;
    }

    public void saveHtChange(String changeNo,String module,String moduleType,String hosLevel){
        String[] modules=module.split(":");
        Optional<String> op=Optional.ofNullable(modules[0]);
        TbProModule tbProModule=tbProModuleMapper.selectProCode(op.orElse("0"));
        Optional<TbProModule> opt=Optional.ofNullable(tbProModule);
        TbHtModuleChange tbHtModuleChange=new TbHtModuleChange();
        tbHtModuleChange.setCreated(new Date());
        tbHtModuleChange.setHtNo(changeNo);
        tbHtModuleChange.setModId(op.orElse("0"));
        tbHtModuleChange.setOperator(VisitInfoHolder.getShowName());
        tbHtModuleChange.setProCode(opt.orElse(null).getProCode());

        TbProModulePrice tbProModulePrice=new TbProModulePrice();

        tbProModulePrice.setModId(op.orElse("0"));
        tbProModulePrice.setHospitalLevel(hosLevel);
        tbProModulePrice=tbProModulePriceMapper.selectStandardPrice(tbProModulePrice);
        //设置标准价
        if(tbProModulePrice==null){
            tbHtModuleChange.setPrice(1.0);
        }else{
            tbHtModuleChange.setPrice(tbProModulePrice.getStandardPrice());
        }
        if(moduleType.equals("new")){
            TbHtModule tbHtModule=new TbHtModule();
            tbHtModule.setCreated(new Date());
            tbHtModule.setOperator(VisitInfoHolder.getShowName());
            tbHtModule.setModId(op.orElse("0"));
            tbHtModule.setProCode(opt.orElse(null).getProCode());
            tbHtModule.setHtNo(changeNo);
            tbHtModule.setPrice(tbHtModuleChange.getPrice());
            tbHtModuleMapper.saveHtModule(tbHtModule);
        }
        tbHtModuleChange.setType(moduleType);
        tbHtModuleChangeMapper.saveHtModuleChange(tbHtModuleChange);
    }

    public Map<String,Object> getHtChangeView(String htNo){
        Map<String,Object> param=new HashMap<>();
        param.put("newModule",tbHtModuleChangeMapper.selectNewModuleList(htNo));
        param.put("oldModule",tbHtModuleChangeMapper.selectOldModuleList(htNo));
        return param;
    }


}
