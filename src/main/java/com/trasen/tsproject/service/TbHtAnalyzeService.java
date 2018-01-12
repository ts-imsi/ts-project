package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtAnalyzeMapper;
import com.trasen.tsproject.dao.TbHtResolveMapper;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.dao.TbPersonnelMapper;
import com.trasen.tsproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/17
 */
@Service
public class TbHtAnalyzeService {

    @Autowired
    private TbHtAnalyzeMapper tbHtAnalyzeMapper;

    @Autowired
    private TbPersonnelMapper tbPersonnelMapper;

    @Autowired
    private TbHtResolveMapper tbHtResolveMapper;

    @Autowired
    private TbMsgMapper tbMsgMapper;


    public Map<String,Object> selectAnalyzeList(String htNo){
        Map<String,Object> resultMap = new HashMap<>();
        List<TbHtAnalyze> tbHtAnalyzeList= tbHtAnalyzeMapper.selectAnalyzeList(htNo);
        List<Select> data = tbPersonnelMapper.selectTbPersonnelList();
        List<Select> selectJson = tbHtAnalyzeMapper.getSelectJson(htNo);
        resultMap.put("list",tbHtAnalyzeList);
        resultMap.put("data",data);
        resultMap.put("selectJson",selectJson);

        return resultMap;
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean saveAnaly(List<TbHtAnalyze> tbHtAnalyzes,List<TbMsg> tbMsgs){
        boolean boo=false;
        //删除数据
        tbHtAnalyzeMapper.deleteAnaly(tbHtAnalyzes.get(0).getHtNo());
        //保存数据
        for(TbHtAnalyze tbHtAnalyze:tbHtAnalyzes){
            tbHtAnalyze.setStatus(0);
            tbHtAnalyzeMapper.saveAnaly(tbHtAnalyze);
        }
        //发送待阅消息
        for(TbMsg tbMsg : tbMsgs){
            tbMsgMapper.insertMsg(tbMsg);
        }
        //更新分解表分解人数据
        TbHtResolve tbHtResolve=new TbHtResolve();
        tbHtResolve.setHtNo(tbHtAnalyzes.get(0).getHtNo());
        tbHtResolve.setProMan(VisitInfoHolder.getShowName());
        tbHtResolveMapper.updateProMan(tbHtResolve);
        boo=true;
        return boo;
    }
}
