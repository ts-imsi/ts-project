package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbMsgMapper;
import com.trasen.tsproject.dao.TbProjectPlanLogMapper;
import com.trasen.tsproject.dao.TbProjectPlanMapper;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.model.TbProjectPlanLog;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by zhangxiahui on 17/11/1.
 */
@Service
public class ProjectPlanService {

    Logger logger = Logger.getLogger(ProjectPlanService.class);

    @Autowired
    TbProjectPlanMapper tbProjectPlanMapper;

    @Autowired
    TbProjectPlanLogMapper tbProjectPlanLogMapper;

    @Autowired
    private TbHtHandoverMapper tbHtHandoverMapper;

    @Autowired
    private TbMsgMapper tbMsgMapper;


    /**
     * 获取项目计划数据
     *
     * */
    public List<TbProjectPlan> queryProjectPlanList(String handoverId){
        List<TbProjectPlan> list = new ArrayList<>();
        list=tbProjectPlanMapper.getProjectPlanByHandOverId(handoverId);
        return list;
    }

    /**
     * 修改初步计划时间
     *
     * */
    public boolean updatePlanTime(TbProjectPlan plan){
        boolean boo = false;
        if(plan!=null&&plan.getPlanId()!=null){
            TbProjectPlan projectPlan = tbProjectPlanLogMapper.getPlanToId(plan.getPlanId());
            //修改调研时间
            boolean upBoo = false;
            if(plan.getSurveyTime()!=null&&projectPlan.getSurveyTime()!=null
                    &&plan.getSurveyTime().getTime()!=projectPlan.getSurveyTime().getTime()){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getSurveyTime());
                log.setNewTime(plan.getSurveyTime());
                log.setCode("survey");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                upBoo = true;
            }
            //修改进程时间
            if(plan.getApproachTime()!=null&&projectPlan.getApproachTime()!=null
                    &&plan.getApproachTime().getTime()!=projectPlan.getApproachTime().getTime()){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getApproachTime());
                log.setNewTime(plan.getApproachTime());
                log.setCode("approac");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                upBoo = true;
            }
            //修改上线时间
            if(plan.getOnlineTime()!=null&&projectPlan.getOnlineTime()!=null
                    &&plan.getOnlineTime().getTime()!=projectPlan.getOnlineTime().getTime()){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getOnlineTime());
                log.setNewTime(plan.getOnlineTime());
                log.setCode("online");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                upBoo = true;
            }
            //修改验收时间
            if(plan.getCheckTime()!=null&&projectPlan.getCheckTime()!=null
                    &&plan.getCheckTime().getTime()!=projectPlan.getCheckTime().getTime()){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getCheckTime());
                log.setNewTime(plan.getCheckTime());
                log.setCode("check");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                upBoo = true;
            }
            if(upBoo){
                plan.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.updatePlanTime(plan);
                boo = true;
            }
        }
        return boo;
    }

    /**
     * 获取时间修改日志
     *
     * */
    public List<TbProjectPlanLog> queryPlanUpdateLog(String code,Integer planId){
        List<TbProjectPlanLog> list = new ArrayList<>();
        if(code!=null&&planId!=null){
            Map<String,Object> param = new HashMap<>();
            param.put("code",code);
            param.put("planId",planId);
            list = tbProjectPlanLogMapper.queryPlanLogList(param);

        }
        return list;
    }


    public PageInfo<TbHtHandover> selectProjecActualizetList(int page,int rows,TbHtHandover tbHtHandover){
        PageHelper.startPage(page,rows);
        List<TbHtHandover> tbHtHandoverList=tbHtHandoverMapper.selectProjectActualizeList(tbHtHandover);
        PageInfo<TbHtHandover> pagehelper = new PageInfo<TbHtHandover>(tbHtHandoverList);
        return pagehelper;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean saveProjectActualizePlan(List<TbProjectPlan> tbProjectPlanList){
        boolean boo=false;
        tbProjectPlanList.stream().forEach(tbProjectPlan -> updateProjectActualizePlan(tbProjectPlan));
        boo=true;
        return boo;
    }

    public void updateProjectActualizePlan(TbProjectPlan tbProjectPlan){
        tbProjectPlan.setOperator(VisitInfoHolder.getShowName());
        tbProjectPlan.setCreated(new Date());
        tbProjectPlanMapper.updateProjectActualizePlan(tbProjectPlan);
        TbHtHandover tbHtHandover=tbHtHandoverMapper.getHandoverToPkid(Integer.valueOf(tbProjectPlan.getHandoverId()));
        TbMsg tbMsg=new TbMsg();
        tbMsg.setHtNo(tbHtHandover.getHtNo());
        tbMsg.setMsgContent(tbHtHandover.getHtNo()+tbHtHandover.getHtName()+tbProjectPlan.getProName()+"实施计划");
        tbMsg.setSendName(VisitInfoHolder.getShowName());
        tbMsg.setType("read");
        tbMsg.setStatus(0);
        tbMsg.setSendTime(new Date());
        tbMsg.setWorkNum(tbProjectPlan.getWorkNum());
        tbMsg.setName(tbProjectPlan.getActualizeManager());
        tbMsg.setTitle(tbHtHandover.getHtNo()+tbProjectPlan.getProName()+"实施计划");
        tbMsgMapper.insertMsg(tbMsg);
        //todo 发送邮箱或者微信
    }

    public boolean selectCountManage(String handoverId){
        int count= tbProjectPlanMapper.selectCountManage(handoverId);
        if(count>0){
            return true;
        }else{
            return false;
        }
    }



}
