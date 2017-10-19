package com.trasen.tsproject.dao;

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

    Integer countMsgByTaskId(String taskId);

    int insertMsg(TbMsg tbMsg);
}