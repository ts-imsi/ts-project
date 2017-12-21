package com.trasen.tsproject.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

import java.math.BigDecimal;
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

    public TbHtHandover getHandoverToHtNo(TbHtHandover handover){
        TbHtHandover tbHtHandover = null;
        if(handover!=null&&!StringUtil.isEmpty(handover.getHtNo())){
            tbHtHandover = htHandoverMapper.getHandover(handover);
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
            TbHtHandover handover = new TbHtHandover();
            handover.setHtNo(contractInfo.getContractNo());
            handover.setType(contractInfo.getType());
            handover.setChangeNo(contractInfo.getChangeNo());
            tbHtHandover = getHandoverToHtNo(handover);
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
                tbHtHandover.setType(contractInfo.getType());
                tbHtHandover.setHtOwner(contractInfo.getContractOwner());
                tbHtHandover.setSignDate(DateUtils.getDate(contractInfo.getSignDate(),"yyyy-MM-dd"));
                tbHtHandover.setContent(JSON.toJSONString(list));
                tbHtHandover.setCreateUser(VisitInfoHolder.getShowName());
                tbHtHandover.setChangeNo(contractInfo.getChangeNo());
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
            TbHtHandover handover = getHandoverToHtNo(tbHtHandover);
            if(handover!=null){
                tbHtHandover.setOperator(VisitInfoHolder.getShowName());
                htHandoverMapper.updateHandover(tbHtHandover);
                boo = true;
            }else{
                tbHtHandover.setCreateUser(VisitInfoHolder.getShowName());
                htHandoverMapper.insertHandover(tbHtHandover);
                boo = true;
            }
        }
        return boo;
    }

    public boolean submitHandover(TbHtHandover tbHtHandover){
        boolean boo = false;
        if(tbHtHandover!=null&&tbHtHandover.getHtNo()!=null&&tbHtHandover.getPkid()!=null){
            tbHtHandover.setOperator(VisitInfoHolder.getShowName());
            //TODO 流程提交
            String process_start=env.getProperty("process_start").replace("{key}","handover");
            if(process_start==null){
                logger.info("交接单提交获取process_start失败");
                return false;
            }
            Map<String,Object> param=new HashMap<String,Object>();
            if(tbHtHandover.getChangeNo()!=null){
                param.put("htNo",tbHtHandover.getChangeNo());
                param.put("handOverId",tbHtHandover.getPkid());
                param.put("htOwner",tbHtHandover.getHtOwner());
            }else{
                return false;
            }

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
                        // TODO: 17/10/26 思考并更增补单 如何加载分解信息
                        List<TbHtResolve> resolveList = contractProductService.queryHtResolve(htHandover.getHtNo());

                        for(TbHtResolve tbHtResolve : resolveList){
                            TempDataVo tempDataVo = new TempDataVo();
                            tempDataVo.setName(tbHtResolve.getProName());
                            tempDataVo.setValue(tbHtResolve.getOutputValue());
                            tempDataVo.setTotal(tbHtResolve.getTotal());
                            if(tbHtResolve.getTotal()!=null){
                                total = add(total,tbHtResolve.getTotal());
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
                        if(item.getModule().equals(item.getName())){
                            tempDataVo.setModule("nbsp");
                        }else{
                            tempDataVo.setModule(item.getModule());
                        }
                        tempDataVo.setValue(Optional.ofNullable(item.getValue()).orElse("nbsp"));
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
            resultMap.put("handover",htHandover);

        }
        return resultMap;
    }

    public Map<String,Object> getProModuleList(Integer handId){
        Map<String,Object> reMap = new HashMap<>();
        Map<String,List<String>> resultMap = new HashMap<>();
        List<ProModuleVo> proModuleVoList = new ArrayList<>();
        TbHtHandover htHandover = getHandoverToPkid(handId);
        if(htHandover!=null&&htHandover.getChangeNo()!=null){
            List<Map<String,Object>> moduleList = htHandoverMapper.getProModuleList(htHandover.getChangeNo());
            for(Map<String,Object> map : moduleList){
                if(map!=null&&map.get("proName")!=null&&map.get("modName")!=null){
                    String proName = map.get("proName").toString();
                    String modName = map.get("modName").toString();
                    if(resultMap.get(proName)!=null){
                        List<String> list = resultMap.get(proName);
                        list.add(modName);
                    }else{
                        List<String> list = new ArrayList<>();
                        list.add(modName);
                        resultMap.put(proName,list);}
                }
            }

            Set<String> proSet = resultMap.keySet();
            for(String proName : proSet){
                List<String> list = resultMap.get(proName);
                ProModuleVo moduleVo = new ProModuleVo();
                moduleVo.setProName(proName);
                moduleVo.setModList(list);
                moduleVo.setSize(list.size());
                proModuleVoList.add(moduleVo);
            }
        }
        reMap.put("list",proModuleVoList);
        reMap.put("handover",htHandover);
        return reMap;
    }


    public List<TimeLineVo> getTimeLine(String processId){
        List<TimeLineVo> list = new ArrayList<>();
        if(processId!=null){
            String timeLine = env.getProperty("time_line").replace("{processId}",processId);
            if(timeLine==null){
                logger.info("==获取流程进度数据,远程服务链接未配置==");
                return list;
            }
            Map<String,Object> paramMap=new HashMap<String,Object>();
            String parameterJson = JSONObject.toJSONString(paramMap);
            String json= HttpUtil.connectURL(timeLine,parameterJson,"POST");
            JSONObject dataJson = (JSONObject) JSONObject.parse(json);
            if(dataJson.getInteger("code")==1){
                JSONArray jsonArray = dataJson.getJSONArray("list");
                for(java.util.Iterator tor=jsonArray.iterator();tor.hasNext();) {
                    TimeLineVo timeLineVo = new TimeLineVo();
                    JSONObject jsonObject = (JSONObject) tor.next();
                    String taskId = jsonObject.getString("id");
                    if(taskId!=null){
                        timeLineVo.setTaskId(taskId);
                        String taskKey = jsonObject.getString("taskDefKey");
                        /*if("sale_commit".equals(taskKey)){
                            timeLineVo.setColour("primary");
                        }else if("nk_check".equals(taskKey)){
                            timeLineVo.setColour("warning");
                        }else if("em_check".equals(taskKey)){
                            timeLineVo.setColour("success");
                        }else if("pd_check".equals(taskKey)){
                            timeLineVo.setColour("white");
                        }else if("pm_check".equals(taskKey)){
                            timeLineVo.setColour("info");
                        }else if("gm_check".equals(taskKey)){
                            timeLineVo.setColour("danger");
                        }else{
                            timeLineVo.setColour("white");
                        }*/
                        timeLineVo.setTaskKey(taskKey);
                        timeLineVo.setTaskName(jsonObject.getString("name"));
                        timeLineVo.setAssignee(jsonObject.getString("assignee"));
                        timeLineVo.setStartTime(jsonObject.getString("startTime"));
                        timeLineVo.setTime(DateUtils.getTime(jsonObject.getString("startTime"),"yyyy-MM-dd HH:mm:ss","MM月dd日"));
                        timeLineVo.setEndTime(jsonObject.getString("endTime"));
                        timeLineVo.setDeleteReason(jsonObject.getString("deleteReason"));
                        List<TbMsg> tbMsgList = htHandoverMapper.getMsgToTaskId(taskId);
                        String content = "";
                        String name = "";
                        if(tbMsgList.size()>1){
                            //生成部门确认
                            timeLineVo.setTitle(tbMsgList.get(0).getTitle());
                            name = "生产部门";
                            for(TbMsg msg : tbMsgList){
                                String str = "";
                                if(msg.getUpdated()==null&&msg.getStatus()==0){
                                    str = "未确认";
                                }else{
                                    str = DateUtils.getTime(msg.getUpdated(),"yyyy-MM-dd HH:mm:ss")+" 确认";
                                }
                                content = content + "确认人:"+msg.getName()+","+str+";";

                            }
                        }else if(tbMsgList.size()==1){
                            TbMsg msg = tbMsgList.get(0);
                            name = msg.getName();
                            content = msg.getRemark();
                            timeLineVo.setTitle(msg.getTitle());
                        }
                        timeLineVo.setRemark(content);
                        timeLineVo.setName(name);
                        if(!StringUtil.isEmpty(timeLineVo.getRemark())&&!"审批通过".equals(timeLineVo.getRemark())&&!"已确认".equals(timeLineVo.getRemark())){
                            //驳回
                            timeLineVo.setColour("danger");
                        }else if(timeLineVo.getEndTime()==null){
                            //待审批
                            timeLineVo.setColour("light");
                        }else{
                            //通过
                            timeLineVo.setColour("info");
                        }
                        list.add(timeLineVo);
                    }
                }
            }
        }
        return list;
    }

    public List<TbHtHandover> queryHandOverList(Map<String,String> param){
        return htHandoverMapper.queryHandOverList(param);
    }

    public void uploadHandoverFile(TbHtHandover htHandover){
        htHandoverMapper.uploadHandoverFile(htHandover);
    }


    public double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
}
