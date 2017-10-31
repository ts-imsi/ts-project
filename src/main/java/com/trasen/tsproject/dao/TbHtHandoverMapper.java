package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbHtHandoverMapper extends MyMapper<TbHtHandover> {

    TbHtHandover getHandover(TbHtHandover handover);

    int insertHandover(TbHtHandover tbHtHandover);

    int updateHandover(TbHtHandover tbHtHandover);

    int submitHandover(TbHtHandover tbHtHandover);

    List<TbHtHandover> getHtHandoverList(TbHtHandover tbHtHandover);

    TbHtHandover getHandoverToPkid(Integer pkid);

    List<TbMsg> getMsgToTaskId(String taskId);

    List<TbHtHandover> selectProjectArrangeList(TbHtHandover tbHtHandover);
}