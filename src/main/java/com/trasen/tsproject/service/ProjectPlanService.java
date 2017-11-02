package com.trasen.tsproject.service;

import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbProjectPlanLogMapper;
import com.trasen.tsproject.dao.TbProjectPlanMapper;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.model.TbProjectPlanLog;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 获取项目计划初始划数据
     *
     * */
    public List<TbProjectPlan> queryProjectPlanList(){
        List<TbProjectPlan> list = new ArrayList<>();
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










}
