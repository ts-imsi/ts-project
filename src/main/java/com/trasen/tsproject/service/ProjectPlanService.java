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
    public TbProjectPlan updatePlanTime(TbProjectPlan plan){
        if(plan!=null&&plan.getPlanId()!=null){
            TbProjectPlan projectPlan = tbProjectPlanLogMapper.getPlanToId(plan.getPlanId());
            //修改调研时间
            boolean upBoo = false;
            if(plan.getWorkNum()!=null&&projectPlan.getWorkNum()!=null
                    &&!plan.getWorkNum().equals(projectPlan.getWorkNum())){
                upBoo = true;
            }
            if(plan.getSurveyTime()!=null&&projectPlan.getSurveyTime()!=null
                    &&!plan.getSurveyTime().equals(projectPlan.getSurveyTime())){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setType("projectPlan");
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getSurveyTime());
                log.setNewTime(plan.getSurveyTime());
                log.setCode("survey");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                plan.setIsSurvey(1);
                upBoo = true;
            }
            //修改进程时间
            if(plan.getApproachTime()!=null&&projectPlan.getApproachTime()!=null
                    &&!plan.getApproachTime().equals(projectPlan.getApproachTime())){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setType("projectPlan");
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getApproachTime());
                log.setNewTime(plan.getApproachTime());
                log.setCode("approac");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                plan.setIsApproach(1);
                upBoo = true;
            }
            //修改上线时间
            if(plan.getOnlineTime()!=null&&projectPlan.getOnlineTime()!=null
                    &&!plan.getOnlineTime().equals(projectPlan.getOnlineTime())){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setType("projectPlan");
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getOnlineTime());
                log.setNewTime(plan.getOnlineTime());
                log.setCode("online");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                plan.setIsOnline(1);
                upBoo = true;
            }
            //修改验收时间
            if(plan.getCheckTime()!=null&&projectPlan.getCheckTime()!=null
                    &&!plan.getCheckTime().equals(projectPlan.getCheckTime())){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setType("projectPlan");
                log.setPlanId(plan.getPlanId());
                log.setOldTime(projectPlan.getCheckTime());
                log.setNewTime(plan.getCheckTime());
                log.setCode("check");// TODO: 17/11/2 类型写入常量类
                log.setRemark(plan.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                plan.setIsCheck(1);
                upBoo = true;
            }
            if(upBoo){
                plan.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.updatePlanTime(plan);
            }
        }
        return plan;
    }

    /**
     * 获取时间修改日志
     *
     * */
    public List<TbProjectPlanLog> queryPlanUpdateLog(String code,Integer planId,String type){
        List<TbProjectPlanLog> list = new ArrayList<>();
        if(code!=null&&planId!=null){
            Map<String,Object> param = new HashMap<>();
            param.put("code",code);
            param.put("planId",planId);
            param.put("type",type);
            list = tbProjectPlanLogMapper.queryPlanLogList(param);

        }
        return list;
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
        tbHtHandover.setIsProPlan(1);
        tbHtHandover.setProPlanTime(new Date());
        tbHtHandoverMapper.updateProPlan(tbHtHandover);
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


    public TbHtHandover selectHandOverByPlanId(Integer planId){
        return tbProjectPlanMapper.selectHandOverByPlanId(planId);
    }


}
