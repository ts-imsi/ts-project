package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbMsgMapper extends MyMapper<TbMsg> {
    List<TbMsg> selectTbMsg(Map<String,String> param);
    TbMsg getTbMsgById(Integer pkid);
    int updateTbMsgStatus(Integer pkid);
    int updateTbMsgStatusAndRemark(TbMsg tbMsg);

    List<Map<String,String>> getPersonTags(String userId);

    Integer countMsgByTaskId(Map<String,Object> param);

    int insertMsg(TbMsg tbMsg);

    int confirmAnalyze(Map<String,Object> param);

    Integer queryNoConfirm(String processId);

    int getButtonStatus(Map<String,String> param);

    TbHtHandover getHandOverToProcessId(String processId);

    List<String> getProcessToAnalyze(String workNum);

    List<TbHtAnalyze> queryAnalyze(String processId);

    int pbReCheck(String processId);

    int updateNowStep(Map<String,Object> para);

    int updateHtChangeNowStep(Map<String,Object> para);

    TbHtChange gethtChangeToProcessId(String processId);

    int updatehtChangeStatus(Map<String,String> param);

    int updateRecount(Map<String,Object> para);

    int updateConfirm(Map<String,Object> para);

    int updateArrange(Map<String,Object> para);

    int updateStatus(Map<String,Object> para);

    int countTodoMsg(String userId);

    List<TbMsg> queryTodoMsgLimit3(String userId);

    List<TbMsg> queryMobileTransactList(Map<String,Object> param);
}