package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.dao.TbProjectManagerMapper;
import com.trasen.tsproject.dao.TbProjectPlanMapper;
import com.trasen.tsproject.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/30
 */
@Service
public class ProjectArrangeService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectArrangeService.class);

    @Autowired
    private TbHtHandoverMapper tbHtHandoverMapper;

    @Autowired
    private TbProjectManagerMapper tbProjectManagerMapper;

    @Autowired
    private TbMsgMapper tbMsgMapper;

    @Autowired
    private TbProjectPlanMapper tbProjectPlanMapper;


    public PageInfo<TbHtHandover> selectProjectArrangeList(int page,int rows,TbHtHandover tbHtHandover){
        PageHelper.startPage(page,rows);
        List<TbHtHandover> tbHtHandoverList=tbHtHandoverMapper.selectProjectArrangeList(tbHtHandover);
        PageInfo<TbHtHandover> pagehelper = new PageInfo<TbHtHandover>(tbHtHandoverList);
        return pagehelper;
    }

    public List<TbProjectManager> getManageByType(String type){
        return tbProjectManagerMapper.getManageByType(type);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean sendLetter(TbHtHandover tbHtHandover,TbProjectManager tbProjectManager){
        boolean boo=false;
        tbHtHandoverMapper.updateProManage(tbHtHandover);
        TbMsg tbMsg=new TbMsg();
        tbMsg.setHtNo(tbHtHandover.getHtNo());
        tbMsg.setMsgContent("项目经理任命书");
        tbMsg.setSendName(VisitInfoHolder.getShowName());
        tbMsg.setType("read");
        tbMsg.setStatus(0);
        tbMsg.setSendTime(new Date());
        tbMsg.setWorkNum(tbProjectManager.getWorkNum());
        tbMsg.setName(tbProjectManager.getName());
        tbMsg.setTitle(tbHtHandover.getHtNo()+"项目经理任命书");
        tbMsgMapper.insertMsg(tbMsg);
        //todo 发送邮箱或者微信
        List<TbProduct> tbProductList=new ArrayList<>();

        tbProductList= tbHtHandoverMapper.getProductByChangeNo(tbHtHandover.getChangeNo());

        if(Optional.ofNullable(tbProductList).isPresent()){
            tbProductList.stream().forEach(tbProduct->saveProjectPlan(tbHtHandover,tbProduct));
        }
        boo=true;
        return boo;
    }
    public void saveProjectPlan(TbHtHandover tbHtHandover,TbProduct tbProduct){
        TbProjectPlan tbProjectPlan=new TbProjectPlan();
        tbProjectPlan.setHandoverId(tbHtHandover.getPkid().toString());
        tbProjectPlan.setHandoverType(tbHtHandover.getType());
        tbProjectPlan.setProCode(tbProduct.getProCode());
        tbProjectPlan.setProName(tbProduct.getProName());
        tbProjectPlanMapper.saveProjectPlan(tbProjectPlan);
    }

}
