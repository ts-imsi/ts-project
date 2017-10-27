package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
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
 * @date 2017/10/13
 */
@Service
public class TbMsgService {
    Logger logger = Logger.getLogger(TbMsgService.class);

    @Autowired
    private TbMsgMapper tbMsgMapper;

    @Autowired
    private Environment env;



    public PageInfo<TbMsg> selectTbMsg(int page,int rows,Map<String,String> param){
        if(param!=null&&param.get("type")!=null&&param.get("status")!=null){
            if("todo".equals(param.get("type"))&&"0".equals(param.get("status"))){
                //待办
                synTodoHandOver(VisitInfoHolder.getUserId(),VisitInfoHolder.getShowName());
            }
        }

        param.put("userId", VisitInfoHolder.getUserId());
        PageHelper.startPage(page,rows);
        List<TbMsg> tbHtChangeList=tbMsgMapper.selectTbMsg(param);
        PageInfo<TbMsg> pagehelper = new PageInfo<TbMsg>(tbHtChangeList);
        return pagehelper;
    }

    public TbMsg getTbMsgById(Integer pkid){
        TbMsg tbMsg  = tbMsgMapper.getTbMsgById(pkid);
        if(tbMsg!=null&&tbMsg.getType().equals("read")&&tbMsg.getStatus()==0){
            updateTbMsgStatus(pkid);
        }
        if(tbMsg!=null&&tbMsg.getType().equals("todo")&&tbMsg.getStatus()==0){
            String process_task=env.getProperty("process_task").replace("{taskId}",tbMsg.getTaskId());
            if(process_task==null){
                logger.info("process_task获取失败");
                return null;
            }else{
                String json= HttpUtil.connectURL(process_task,"","POST");
                JSONObject dataJson = (JSONObject) JSONObject.parse(json);
                if(dataJson.getInteger("code")==1){
                    JSONObject jsonObject=dataJson.getJSONObject("task");
                    JSONObject variables=jsonObject.getJSONObject("variables");
                    if(variables==null){
                        return null;
                    }else{
                        tbMsg.setHtNo(variables.getString("htNo"));
                        tbMsg.setHandOverId(variables.getString("handOverId"));
                    }
                }
            }
            Map<String,String> params=new HashMap<>();
            params.put("name",tbMsg.getTaskKey());
            params.put("type","12");
            tbMsg.setReturnStatus(tbMsgMapper.getButtonStatus(params));
            params.put("type","13");
            tbMsg.setResolveStatus(tbMsgMapper.getButtonStatus(params));
            params.put("type","14");
            tbMsg.setProductStatus(tbMsgMapper.getButtonStatus(params));
        }
        return tbMsg;
    }

    public int updateTbMsgStatus(Integer pkid){
       return tbMsgMapper.updateTbMsgStatus(pkid);
    }

    public int updateTbMsgStatusAndRemark(TbMsg tbMsg){
        return tbMsgMapper.updateTbMsgStatusAndRemark(tbMsg);
    }

    public boolean submitFlow(TbMsg tbMsg){
        String process_complete=env.getProperty("process_complete").replace("{id}",tbMsg.getTaskId());
        if(process_complete==null){
            logger.info("process_complete获取失败");
            return false;
        }
        String json= HttpUtil.connectURL(process_complete,"","POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        if(dataJson.getInteger("code")==1){
            logger.info("流程提交成功");
            tbMsg.setStatus(1);
            updateTbMsgStatusAndRemark(tbMsg);
            updateNowStep(dataJson);
        }else{
            logger.info("流程提交失败");
            return false;
        }
        return true;
    }

    public void updateNowStep(JSONObject dataJson){
        JSONObject task = dataJson.getJSONObject("task");
        if(task!=null){
            String processId = task.getString("processId");
            String processKey = task.getString("processKey");
            String nowStep = task.getString("subTitle");
            Integer isConfirm = 0;

            if(processKey!=null){
                String key = processKey.split(":")[0];
                if("handover".equals(key)){
                    if(nowStep==null){
                        nowStep = "完成审批";
                        isConfirm = 1;
                    }
                    Map<String,Object> para = new HashMap<>();
                    para.put("nowStep",nowStep);
                    para.put("isConfirm",isConfirm);
                    para.put("operator",VisitInfoHolder.getShowName());
                    para.put("processId",processId);
                    tbMsgMapper.updateNowStep(para);
                }
            }
        }
    }

    public boolean returnFlow(TbMsg tbMsg){
        String process_return=env.getProperty("process_return").replace("{id}",tbMsg.getTaskId());
        if(process_return==null){
            logger.info("process_return获取失败");
            return false;
        }
        String json= HttpUtil.connectURL(process_return,"","POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        if(dataJson.getInteger("code")==1){
            logger.info("流程回退成功");
            tbMsg.setStatus(1);
            updateTbMsgStatusAndRemark(tbMsg);
            updateNowStep(dataJson);
        }else{
            logger.info("流程回退失败");
            return false;
        }
        return true;
    }


    public boolean pdConfirm(TbMsg tbMsg){
        boolean boo = false;
        if(tbMsg!=null&&tbMsg.getProcessId()!=null){
            Map<String,Object> param = new HashMap();
            param.put("operator",tbMsg.getWorkNum());
            param.put("processId",tbMsg.getProcessId());
            //生产部门确认
            tbMsgMapper.confirmAnalyze(param);
            //生产部门负责人全部确认完之后提交流程节点到下一步
            Integer count = tbMsgMapper.queryNoConfirm(tbMsg.getProcessId());
            if(count==0){
                submitFlow(tbMsg);
            }
            boo = true;
            tbMsg.setStatus(1);
            updateTbMsgStatusAndRemark(tbMsg);
        }
        return boo;
    }




    public void synTodoHandOver(String userId,String showName){
        if(userId!=null){
            List<Map<String,String>> tags = tbMsgMapper.getPersonTags(userId);
            for(Map<String,String> tagMap : tags){
                String tagCode = tagMap.get("tag_id");
                String workNum = tagMap.get("work_num");
                if(tagCode.indexOf("tag_handover")>0){
                    String assignee = tagCode.substring(14,tagCode.length()-1);
                    String wf_todo = env.getProperty("wf_todo").replace("{assignee}",assignee);
                    Map<String,Object> paramMap=new HashMap<String,Object>();
                    if("sale".equals(assignee)){
                        paramMap.put("htOwner",showName);
                    }
                    List<String> processList = null;
                    if("PD".equals(assignee)){
                        processList = tbMsgMapper.getProcessToAnalyze(workNum);
                        if(processList==null){
                            processList = new ArrayList<>();
                        }
                    }
                    String parameterJson = JSONObject.toJSONString(paramMap);
                    String json= HttpUtil.connectURL(wf_todo,parameterJson,"POST");
                    JSONObject dataJson = (JSONObject) JSONObject.parse(json);
                    if(dataJson.getInteger("code")==1){
                        JSONArray jsonArray = dataJson.getJSONArray("list");
                        for(java.util.Iterator tor=jsonArray.iterator();tor.hasNext();) {
                            JSONObject jsonObject = (JSONObject) tor.next();
                            String processId = jsonObject.getString("processId");
                            String processKey=jsonObject.getString("processKey");
                            if(processId!=null&&!"".equals(processId)){
                                //判断生产部门待办,如果是生产部门待办必须参与这个流程才能生成待办
                                if(processList!=null&&!processList.contains(processId)){
                                    continue;
                                }
                                if(processKey.contains("addChange")) todohtChangeMsg(jsonObject,processId,workNum,showName);
                                if(processKey.contains("handover")) todohandOverMsg(jsonObject,processId,workNum,showName);
                            }

                        }
                    }
                }
            }
        }
    }

    public void todohandOverMsg(JSONObject jsonObject,String processId,String workNum,String showName){
        TbMsg msg = new TbMsg();
        TbHtHandover handover = tbMsgMapper.getHandOverToProcessId(processId);
        if(handover!=null){
            String title = "["+handover.getHtNo()+"]"+jsonObject.getString("title");
            String content = "合同["+handover.getHtName()+"],客户["+handover.getCustomerName()+"],"+jsonObject.getString("msgContent");
            msg.setWorkNum(workNum);
            msg.setName(showName);
            msg.setTitle(title);
            msg.setMsgContent(content);
            msg.setSendTime(jsonObject.getDate("sendTime"));
            msg.setProcessId(jsonObject.getString("processId"));
            msg.setProcessKey(jsonObject.getString("processKey"));
            msg.setTaskId(jsonObject.getString("taskId"));
            msg.setTaskKey(jsonObject.getString("taskKey"));
            msg.setType("todo");
            msg.setStatus(0);
            msg.setSendName("ts-workflow");

            Map<String,Object> param  = new HashMap();
            param.put("taskId",msg.getTaskId());
            param.put("workNum",workNum);
            Integer num = tbMsgMapper.countMsgByTaskId(param);
            if(num==0){
                tbMsgMapper.insertMsg(msg);
            }
        }
    }

    public void todohtChangeMsg(JSONObject jsonObject,String processId,String workNum,String showName){
        TbMsg msg = new TbMsg();
        TbHtChange tbHtChange = tbMsgMapper.gethtChangeToProcessId(processId);
        if(tbHtChange!=null){
            String title = "["+tbHtChange.getHtNo()+"]"+jsonObject.getString("title");
            String content = "合同["+tbHtChange.getHtName()+"],客户["+tbHtChange.getCustomerName()+"],"+jsonObject.getString("msgContent");
            content = content + "变更理由:"+tbHtChange.getChangeContent();
            content = content + "变更说明:"+tbHtChange.getRemark();
            msg.setWorkNum(workNum);
            msg.setName(showName);
            msg.setTitle(title);
            msg.setMsgContent(content);
            msg.setSendTime(jsonObject.getDate("sendTime"));
            msg.setProcessId(jsonObject.getString("processId"));
            msg.setProcessKey(jsonObject.getString("processKey"));
            msg.setTaskId(jsonObject.getString("taskId"));
            msg.setTaskKey(jsonObject.getString("taskKey"));
            msg.setType("todo");
            msg.setStatus(0);
            msg.setSendName("ts-workflow");

            Map<String,Object> param  = new HashMap();
            param.put("taskId",msg.getTaskId());
            param.put("workNum",workNum);
            Integer num = tbMsgMapper.countMsgByTaskId(param);
            if(num==0){
                tbMsgMapper.insertMsg(msg);
            }
        }
    }

    public boolean checkAnalyze(String processId){
        boolean boo = false;
        if(processId!=null){
            List<TbHtAnalyze> list = tbMsgMapper.queryAnalyze(processId);
            if(list!=null&&list.size()>0){
                boo = true;
            }
        }
        return boo;
    }

    public boolean pbReCheck(String processId){
        boolean boo = false;
        if(processId!=null){
            tbMsgMapper.pbReCheck(processId);
            boo = true;
        }
        return boo;
    }

    public boolean updatehtChangeStatus(Map<String,String> param){
        boolean boo=false;
        if(!param.isEmpty()){
            tbMsgMapper.updatehtChangeStatus(param);
            boo=true;
        }
        return boo;
    }

}
