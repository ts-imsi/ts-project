package com.trasen.tsproject.service;

import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbPlanDetailMapper;
import com.trasen.tsproject.dao.TbPlanItemMapper;
import com.trasen.tsproject.model.TbPlanDetail;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.model.TbProjectPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                String tempStr = "";
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
                for(TbPlanItem planItem: list){
                    if(!tempStr.equals(planItem.getStageName())){
                        TbPlanItem item = new TbPlanItem();
                        item.setStageName(planItem.getStageName());
                        item.setPlanStartTime(planItem.getPlanStartTime());
                        tempStr = planItem.getStageName();
                        planItems.add(item);
                    }else{
                        planItem.setStageName("");
                    }
                    planItems.add(planItem);


                }




                detail.setTbPlanItems(planItems);
            }
        }

        return detail;
    }









}
