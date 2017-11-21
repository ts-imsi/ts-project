package com.trasen.tsproject.service;

import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbPlanDetailMapper;
import com.trasen.tsproject.dao.TbPlanItemMapper;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.model.TbPlanStage;
import com.trasen.tsproject.model.TbProjectPlan;
import com.trasen.tsproject.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zhangxiahui on 17/11/16.
 */
@Service
public class PlanDetailService {

    private static final Logger logger = LoggerFactory.getLogger(PlanDetailService.class);

    @Autowired
    TbPlanDetailMapper tbPlanDetailMapper;

    @Autowired
    TbPlanItemMapper tbPlanItemMapper;

    public TbPlanDetail getPlanItemList(Integer planId, String type){
        TbPlanDetail detail = null;
        if(planId!=null){
            TbProjectPlan projectPlan = tbPlanDetailMapper.getProjectPlan(planId);
            if(projectPlan!=null&&projectPlan.getProCode()!=null&&projectPlan.getPlanId()!=null&&type!=null){
                List<TbPlanItem> planItems = new ArrayList<>();
                List<TbPlanItem> list = new ArrayList<>();
                detail = tbPlanDetailMapper.getPlanDetail(projectPlan.getPlanId());
                if(detail==null){
                    detail = new TbPlanDetail();
                    detail.setPlanId(projectPlan.getPlanId());
                    detail.setOperator(VisitInfoHolder.getShowName());
                    tbPlanDetailMapper.insertPlanDetail(detail);


                    Map<String,Object> param = new HashMap<>();
                    param.put("proCode",projectPlan.getProCode());
                    param.put("type",type);
                    list = tbPlanItemMapper.queryTemplatePlanItems(param);
                    for(TbPlanItem item : list){
                        item.setPlanId(planId);
                        item.setDetailId(detail.getPkid());
                        item.setOperator(VisitInfoHolder.getShowName());
                        tbPlanItemMapper.insertPlanItem(item);
                    }
                }else{
                    Map<String,Object> itemMap = new HashMap<>();
                    itemMap.put("planId",detail.getPlanId());
                    itemMap.put("detailId",detail.getPkid());
                    list = tbPlanItemMapper.queryPlanItems(itemMap);
                }
                Map<String,TbPlanStage> stageMap = new HashMap<>();
                for(TbPlanItem planItem: list){
                    TbPlanStage stage;
                    if(stageMap.get(planItem.getStageName())==null){
                        stage = new TbPlanStage();
                        List<TbPlanItem> tbPlanItems = new ArrayList<>();
                        tbPlanItems.add(planItem);
                        stage.setTbPlanItems(tbPlanItems);
                        stage.setStageName(planItem.getStageName());
                        if(planItem.getPlanTime()!=null){
                            stage.setPlanStartTime(DateUtils.getDate(planItem.getPlanTime(),"yyyy-MM-dd"));
                        }
                        stageMap.put(planItem.getStageName(),stage);
                    }else{
                        stage = stageMap.get(planItem.getStageName());
                        stage.getTbPlanItems().add(planItem);
                        if(planItem.getPlanTime()!=null){
                            if(stage.getPlanStartTime()!=null){
                                stage.setPlanEndTime(DateUtils.getDate(planItem.getPlanTime(),"yyyy-MM-dd"));
                            }else{
                                stage.setPlanStartTime(DateUtils.getDate(planItem.getPlanTime(),"yyyy-MM-dd"));
                            }
                        }
                    }
                }
                List<TbPlanStage> stageList = new ArrayList<>();
                stageList.addAll(stageMap.values());
                detail.setTbPlanStages(stageList);
            }
        }
        return detail;
    }


    public void savePlanDetail(TbPlanDetail tbPlanDetail){
        if(tbPlanDetail!=null&&tbPlanDetail.getPkid()!=null){
            tbPlanDetail.setOperator(VisitInfoHolder.getShowName());
            tbPlanDetailMapper.updatePlanDetail(tbPlanDetail);
        }
    }









}
