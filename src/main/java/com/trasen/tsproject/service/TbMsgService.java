package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.model.TbMsg;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PageInfo<TbMsg> selectTbMsg(int page,int rows,Map<String,String> param){
        //TODO VisitInfoHolder.getUserId()
        param.put("userId","3");
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
        return tbMsg;
    }

    public int updateTbMsgStatus(Integer pkid){
       return tbMsgMapper.updateTbMsgStatus(pkid);
    }

}
