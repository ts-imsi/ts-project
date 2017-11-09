package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.model.TbProduct;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;
import java.util.Map;

public interface TbHtHandoverMapper extends MyMapper<TbHtHandover> {

    TbHtHandover getHandover(TbHtHandover handover);

    int insertHandover(TbHtHandover tbHtHandover);

    int updateHandover(TbHtHandover tbHtHandover);

    int submitHandover(TbHtHandover tbHtHandover);

    List<TbHtHandover> getHtHandoverList(TbHtHandover tbHtHandover);

    TbHtHandover getHandoverToPkid(Integer pkid);

    List<TbMsg> getMsgToTaskId(String taskId);

    List<TbHtHandover> selectProjectArrangeList(TbHtHandover tbHtHandover);


    int updateProManage(TbHtHandover tbHtHandover);

    List<TbProduct> getProductByHtNo(String htNo);
    List<TbProduct> getProductByChangeNo(String changeNo);

    int updateProPlan(TbHtHandover tbHtHandover);

    TbHtHandover selectByProcessId(String processId);

    List<String> queryHandOverByOwerOfType(Map<String,String> param);
}