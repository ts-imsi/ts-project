package com.trasen.tsproject.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbTemplate;
import com.trasen.tsproject.model.TbTemplateItem;
import com.trasen.tsproject.util.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangxiahui on 17/9/27.
 */
@Service
public class HandoverService {

    Logger logger = Logger.getLogger(HandoverService.class);

    @Autowired
    TbHtHandoverMapper htHandoverMapper;

    @Autowired
    TemplateService templateService;

    public TbHtHandover getHandoverToHtNo(String htNo){
        TbHtHandover tbHtHandover = null;
        if(!StringUtil.isEmpty(htNo)){
            tbHtHandover = htHandoverMapper.getHandoverToHtNo(htNo);
            if(tbHtHandover!=null&&tbHtHandover.getContent()!=null){
                List<TbTemplateItem> list = JSON.parseArray(tbHtHandover.getContent(), TbTemplateItem.class);
                tbHtHandover.setContentJson(list);
            }
        }
        return tbHtHandover;
    }

    public List<TbTemplateItem> htAndHandover(ContractInfo contractInfo,List<TbTemplateItem> list){
        if(contractInfo==null||list==null||list.size()<1){
            return null;
        }
        for(TbTemplateItem item : list){
            if("customerName".equals(item.getCode())){
                item.setValue(contractInfo.getCustomerName());
            }
            if("contactAddress".equals(item.getCode())){
                item.setValue(contractInfo.getContactAddress());
            }
            if("linkman".equals(item.getCode())){
                item.setValue(contractInfo.getBuyerSigner());
            }
            if("contactPhone".equals(item.getCode())){
                item.setValue(contractInfo.getContactPhone());
            }

            if("contractNo".equals(item.getCode())){
                item.setValue(contractInfo.getContractNo());
            }
            if("contractName".equals(item.getCode())){
                item.setValue(contractInfo.getContractName());
            }
            if("buyer".equals(item.getCode())){
                item.setValue(contractInfo.getBuyer());
            }
            if("seller".equals(item.getCode())){
                item.setValue(contractInfo.getSeller());
            }
            if("contractPrice".equals(item.getCode())){
                if(contractInfo.getContractPrice()!=null){
                    item.setValue(contractInfo.getContractPrice().toString());
                }
            }
            if("payMode".equals(item.getCode())){
                item.setValue(contractInfo.getPaymode());
            }
        }
        return list;
    }


    public TbHtHandover getHandover(ContractInfo contractInfo){
        TbHtHandover tbHtHandover = new TbHtHandover();
        if(contractInfo==null){
            return tbHtHandover;
        }
        if(contractInfo.getContractNo()!=null){
            tbHtHandover = getHandoverToHtNo(contractInfo.getContractNo());
            if(tbHtHandover!=null){
                return tbHtHandover;
            }
            TbTemplate tbTemplate = templateService.getTemplate("handover");
            if(tbTemplate!=null&&tbTemplate.getContentJson()!=null&&tbTemplate.getContentJson().size()>0){
                //模板和合同数据匹配
                List<TbTemplateItem> list = htAndHandover(contractInfo,tbTemplate.getContentJson());
                tbHtHandover = new TbHtHandover();
                tbHtHandover.setContentJson(list);
                tbHtHandover.setHtNo(contractInfo.getContractNo());
                tbHtHandover.setHtName(contractInfo.getContractName());
                tbHtHandover.setCustomerName(contractInfo.getCustomerName());
                // TODO: 17/9/28 交接单类型需定义
                tbHtHandover.setType("new");
                tbHtHandover.setHtOwner(contractInfo.getContractOwner());
                tbHtHandover.setSignDate(DateUtils.getDate(contractInfo.getSignDate(),"yyyy-MM-dd"));
                tbHtHandover.setContent(JSON.toJSONString(list));
                tbHtHandover.setCustomerName(VisitInfoHolder.getUserId());
            }
        }
        return tbHtHandover;
    }

    public boolean saveHandover(TbHtHandover tbHtHandover){
        boolean boo = false;
        if(tbHtHandover!=null&&tbHtHandover.getHtNo()!=null){
            if(tbHtHandover.getContentJson()!=null){
                String content = JSON.toJSONString(tbHtHandover.getContentJson());
                tbHtHandover.setContent(content);
            }
            TbHtHandover handover = getHandoverToHtNo(tbHtHandover.getHtNo());
            if(handover!=null){
                tbHtHandover.setOperator(VisitInfoHolder.getUserId());
                htHandoverMapper.updateHandover(tbHtHandover);
                boo = true;
            }else{
                tbHtHandover.setCreateUser(VisitInfoHolder.getUserId());
                htHandoverMapper.insertHandover(tbHtHandover);
                boo = true;
            }
        }
        return boo;
    }

    public boolean submitHandover(TbHtHandover tbHtHandover){
        boolean boo = false;
        if(tbHtHandover!=null&&tbHtHandover.getHtNo()!=null){
            tbHtHandover.setOperator(VisitInfoHolder.getUserId());
            htHandoverMapper.submitHandover(tbHtHandover);
            boo = true;
        }
        return boo;
    }











}
