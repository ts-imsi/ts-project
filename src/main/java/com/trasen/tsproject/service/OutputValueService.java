package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbOutputValueMapper;
import com.trasen.tsproject.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/11/30.
 */
@Service
public class OutputValueService {

    Logger logger = Logger.getLogger(OutputValueService.class);

    @Autowired
    TbOutputValueMapper tbOutputValueMapper;

    @Autowired
    TbHtChangeService tbHtChangeService;

    public void addOutputValue(TbPlanItem item){
        if(item!=null&&item.getPlanId()!=null){
            TbOutputValue outputValue = new TbOutputValue();
            outputValue.setItemId(item.getPkid());
            outputValue.setOutput(item.getOutput());
            outputValue.setDocName(item.getDocName());
            outputValue.setOperator(VisitInfoHolder.getShowName());
            TbProjectPlan plan = tbOutputValueMapper.getProjectPlan(item.getPlanId());
            if(plan!=null&&plan.getHandoverId()!=null){
                outputValue.setProName(plan.getProName());
                outputValue.setProCode(plan.getProCode());
                TbHtHandover handover = tbOutputValueMapper.getHtHandOver(plan.getHandoverId());
                if(handover.getChangeNo()!=null&&plan.getProCode()!=null){
                    outputValue.setHtNo(handover.getHtNo());
                    outputValue.setHtName(handover.getHtName());
                    outputValue.setCustomerName(handover.getCustomerName());
                    Map<String,Object> param = new HashMap<>();
                    param.put("changeNo",handover.getChangeNo());
                    param.put("proCode",plan.getProCode());
                    TbHtResolve resolve = tbOutputValueMapper.getHtResolve(param);
                    Double total = resolve.getTotal();
                    if(total!=null){
                        outputValue.setTotal(total);
                        if(item.getOutput()!=null){
                            outputValue.setSubtotal(total*item.getOutput());
                        }
                    }
                }
                TbProduct product = tbOutputValueMapper.getProduct(plan.getProCode());
                outputValue.setDepId(product.getDepId());
                outputValue.setDepName(product.getDepName());
            }
            outputValue.setStatus(0);
            tbOutputValueMapper.insertOutputValue(outputValue);
        }
    }


    public boolean updateOutputValue(List<TbOutputValue> outputList){
        boolean boo = false;
        if(outputList!=null){
            for (TbOutputValue outputValue : outputList){
                outputValue.setOperator(VisitInfoHolder.getShowName());
                tbOutputValueMapper.updateOutputValue(outputValue);
            }
            boo = true;
        }
        return boo;
    }



    public PageInfo<OutputValueVo> queryOutputValueDept(int page, int rows, TbOutputValue outputValue){
        PageHelper.startPage(page,rows);
        List<OutputValueVo> outputValueVoList = tbOutputValueMapper.queryOutputValueToDept(outputValue);
        /*for(OutputValueVo outputValueVo : outputValueVoList){
            TbOutputValue tbOutputValue = new TbOutputValue();
            tbOutputValue.setDepId(outputValueVo.getId());
            tbOutputValue.setStatus(outputValue.getStatus());
            List<TbOutputValue> tbOutputValues = tbOutputValueMapper.queryOutputValue(tbOutputValue);
            outputValueVo.setOutputValueList(tbOutputValues);
        }*/
        outputValueVoList.stream().forEach(outputValueVo -> setOutputValueList(outputValueVo,outputValue));
        PageInfo<OutputValueVo> pagehelper = new PageInfo<>(outputValueVoList);
        return pagehelper;
    }

    public PageInfo<OutputValueVo> queryOutputValueHT(int page, int rows, TbOutputValue outputValue){
        PageHelper.startPage(page,rows);
        List<OutputValueVo> outputValueVoList = tbOutputValueMapper.queryOutputValueToHT(outputValue);
        for(OutputValueVo outputValueVo : outputValueVoList){
            TbOutputValue tbOutputValue = new TbOutputValue();
            tbOutputValue.setHtNo(outputValueVo.getId());
            tbOutputValue.setStatus(outputValue.getStatus());
            List<TbOutputValue> tbOutputValues = tbOutputValueMapper.queryOutputValue(tbOutputValue);
            outputValueVo.setOutputValueList(tbOutputValues);
        }
        PageInfo<OutputValueVo> pagehelper = new PageInfo<>(outputValueVoList);
        return pagehelper;
    }


    public List<OutputValueVo> selectOutputValueDept(TbOutputValue outputValue){
        List<OutputValueVo> outputValueVoList = tbOutputValueMapper.queryOutputValueToDept(outputValue);
        outputValueVoList.stream().forEach(outputValueVo -> setOutputValueList(outputValueVo,outputValue));
        return outputValueVoList;
    }

    public void setOutputValueList(OutputValueVo outputValueVo,TbOutputValue outputValue){
        TbOutputValue tbOutputValue = new TbOutputValue();
        tbOutputValue.setDepId(outputValueVo.getId());
        tbOutputValue.setStatus(outputValue.getStatus());
        List<TbOutputValue> tbOutputValues = tbOutputValueMapper.queryOutputValue(tbOutputValue);
        outputValueVo.setOutputValueList(tbOutputValues);
    }

    public List<TbOutputValue> queryProLine(){
        return tbOutputValueMapper.queryProLine();
    }

    public List<TbOutputValue> queryHtProduct(String htNo){
        List<TbOutputValue> list = new ArrayList<>();
        if(htNo != null){
            Map<String,Object> con = tbHtChangeService.getContractByHtNo(htNo);
            if(con!=null&&con.get("object")!=null){
                ContractInfo contractInfo = (ContractInfo) con.get("object");
                list = tbOutputValueMapper.queryHtPro(htNo);
                for(TbOutputValue outputValue : list){
                    outputValue.setHtNo(contractInfo.getContractNo());
                    outputValue.setHtName(contractInfo.getContractName());
                    outputValue.setCustomerName(contractInfo.getCustomerName());
                }
            }
        }
        return list;
    }

    public void insertOutputValue(List<TbOutputValue> outputValueList){
        for(TbOutputValue outputValue : outputValueList){
            tbOutputValueMapper.insertOutputValue(outputValue);
        }
    }

    public Integer findOutputToHtNo(String htNo){
        return tbOutputValueMapper.findOutputToHtNo(htNo);
    }


}
