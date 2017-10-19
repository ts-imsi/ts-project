package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
        param.put("userId", VisitInfoHolder.getUserId());
        PageHelper.startPage(page,rows);
        synTodoHandOver(VisitInfoHolder.getUserId(),VisitInfoHolder.getShowName());

        List<TbMsg> tbHtChangeList=tbMsgMapper.selectTbMsg(param);
        PageInfo<TbMsg> pagehelper = new PageInfo<TbMsg>(tbHtChangeList);
        return pagehelper;
    }

    public TbMsg getTbMsgById(Integer pkid){
        TbMsg tbMsg  = tbMsgMapper.getTbMsgById(pkid);
        if(tbMsg!=null&&tbMsg.getType().equals("read")&&tbMsg.getStatus()==0){
            updateTbMsgStatus(pkid);
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
        }else{
            logger.info("流程提交失败");
            return false;
        }
        return true;
    }

    public boolean returnFlow(TbMsg tbMsg){
        String process_return=env.getProperty("process_return").replace("{id}",tbMsg.getProcessId());
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
        }else{
            logger.info("流程回退成功");
            return false;
        }
        return true;
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
                    String parameterJson = JSONObject.toJSONString(paramMap);
                    String json= HttpUtil.connectURL(wf_todo,parameterJson,"POST");
                    JSONObject dataJson = (JSONObject) JSONObject.parse(json);
                    if(dataJson.getInteger("code")==1){
                        JSONArray jsonArray = dataJson.getJSONArray("list");
                        for(java.util.Iterator tor=jsonArray.iterator();tor.hasNext();) {
                            TbMsg msg = new TbMsg();
                            JSONObject jsonObject = (JSONObject) tor.next();
                            msg.setWorkNum(workNum);
                            msg.setName(showName);
                            msg.setTitle(jsonObject.getString("title"));
                            msg.setMsgContent(jsonObject.getString("msgContent"));
                            msg.setSendTime(jsonObject.getDate("sendTime"));
                            msg.setProcessId(jsonObject.getString("processId"));
                            msg.setProcessKey(jsonObject.getString("processKey"));
                            msg.setTaskId(jsonObject.getString("taskId"));
                            msg.setTaskKey(jsonObject.getString("taskKey"));
                            msg.setType("todo");
                            msg.setStatus(0);
                            msg.setSendName("ts-workflow");
                            Integer num = tbMsgMapper.countMsgByTaskId(msg.getTaskId());
                            if(num==0){
                                tbMsgMapper.insertMsg(msg);
                            }
                        }
                    }
                }
            }
        }

    }

}
