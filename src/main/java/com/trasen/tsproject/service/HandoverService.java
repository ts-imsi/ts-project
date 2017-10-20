package com.trasen.tsproject.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.DateUtils;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    ContractProductService contractProductService;

    @Autowired
    private Environment env;

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
                tbHtHandover.setCreateUser(VisitInfoHolder.getShowName());
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
            //TODO 流程提交
            String process_start=env.getProperty("process_start").replace("{key}","handover");
            if(process_start==null){
                logger.info("交接单提交获取process_start失败");
                return false;
            }
            Map<String,Object> param=new HashMap<String,Object>();
            param.put("htNo",tbHtHandover.getHtNo());
            param.put("handOverId",tbHtHandover.getPkid());
            param.put("htOwner",tbHtHandover.getHtOwner());
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
            if(process_id!=null){
                tbHtHandover.setProcessId(process_id);
            }else{
                logger.info("获取peocess_id失败");
                return false;
            }
            htHandoverMapper.submitHandover(tbHtHandover);
            boo = true;
        }
        return boo;
    }


    public PageInfo<TbHtHandover> getHtHandoverList(int page,int rows,TbHtHandover tbHtHandover){
        PageHelper.startPage(page,rows);
        List<TbHtHandover> tbHtHandoverList=htHandoverMapper.getHtHandoverList(tbHtHandover);
        PageInfo<TbHtHandover> pagehelper = new PageInfo<TbHtHandover>(tbHtHandoverList);
        return pagehelper;

    }

    public TbHtHandover getHandoverToPkid(Integer pkid){
        TbHtHandover tbHtHandover = null;
        if(pkid != null){
            tbHtHandover = htHandoverMapper.getHandoverToPkid(pkid);
            if(tbHtHandover!=null&&tbHtHandover.getContent()!=null){
                List<TbTemplateItem> list = JSON.parseArray(tbHtHandover.getContent(), TbTemplateItem.class);
                tbHtHandover.setContentJson(list);
            }
        }
        return tbHtHandover;
    }

    public Map<String,Object> getTempDataList(Integer pkid){
        Map<String,Object> resultMap = new HashMap<>();
        List<TempDataVo> tempDataVoList = new ArrayList<>();
        Double total = 0d;
        List<TempDataVo> signList = new ArrayList<>();
        TbHtHandover htHandover = getHandoverToPkid(pkid);
        if(htHandover!=null&&htHandover.getContentJson()!=null&&htHandover.getContentJson().size()>0){
            Map<String,List<TempDataVo>> dataMap = new HashMap<>();

            List<TempDataVo> deptList = new ArrayList<>();
            Map<String,List<TempDataVo>> resolveMap = new HashMap<>();
            for(TbTemplateItem item : htHandover.getContentJson()){
                if(item.getLevel()==0&&item.getInput()==null){
                    if("htResolve".equals(item.getCode())){
                        List<TbHtResolve> resolveList = contractProductService.queryHtResolve(htHandover.getHtNo());

                        for(TbHtResolve tbHtResolve : resolveList){
                            TempDataVo tempDataVo = new TempDataVo();
                            tempDataVo.setName(tbHtResolve.getProName());
                            tempDataVo.setValue(tbHtResolve.getOutputValue());
                            tempDataVo.setTotal(tbHtResolve.getTotal());
                            if(tbHtResolve.getTotal()!=null){
                                total = total + tbHtResolve.getTotal();
                            }
                            if(resolveMap.get(tbHtResolve.getDepName())==null){
                                List<TempDataVo> tempDataVos = new ArrayList<>();
                                tempDataVos.add(tempDataVo);
                                resolveMap.put(tbHtResolve.getDepName(),tempDataVos);
                            }else{
                                resolveMap.get(tbHtResolve.getDepName()).add(tempDataVo);
                            }
                        }


                    }else{
                        TempDataVo tempDataVo = new TempDataVo();
                        tempDataVo.setName(item.getName());
                        tempDataVo.setValue(item.getValue());
                        signList.add(tempDataVo);
                    }
                    //过滤非填写内容
                    continue;
                }
                String module = item.getModule();
                TempDataVo vo = new TempDataVo();
                vo.setName(item.getName());
                vo.setLength(10/item.getLevel());
                vo.setValue(item.getValue());
                if(module!=null&&dataMap.get(module)!=null){
                    dataMap.get(module).add(vo);
                }else if (module!=null&&dataMap.get(module)==null){
                    List<TempDataVo> voList = new ArrayList<>();
                    voList.add(vo);
                    dataMap.put(module,voList);
                }
            }
            Set<String> moduleSet = dataMap.keySet();
            for(String module : moduleSet){
                TempDataVo vo = new TempDataVo();
                vo.setName(module);
                List<TempDataVo> voList = dataMap.get(module);
                Integer length = 0;
                List<TempDataVo> tempList = new ArrayList<>();
                for(TempDataVo data : voList){
                    length = length + data.getLength();
                    if(data.getLength()==5){
                        tempList.add(data);
                    }
                }

                if(length%10!=0){
                    tempList.get(tempList.size()-1).setLength(10);
                    length = length + 5;
                }
                vo.setLength(length);
                vo.setVoList(voList);
                tempDataVoList.add(vo);
            }

            Set<String> deptSet = resolveMap.keySet();
            for(String dept : deptSet){
                TempDataVo vo = new TempDataVo();
                vo.setName(dept);
                List<TempDataVo> voList = resolveMap.get(dept);
                vo.setVoList(voList);
                deptList.add(vo);
            }
            TempDataVo tempDataVo = new TempDataVo();
            tempDataVo.setName("合同分解信息");
            tempDataVo.setVoList(deptList);
            tempDataVoList.add(tempDataVo);

            resultMap.put("tempDataVoList",tempDataVoList);
            resultMap.put("total",total);
            resultMap.put("signList",signList);


        }
        return resultMap;
    }
}
