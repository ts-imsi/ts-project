package com.trasen.tsproject.service;

import cn.trasen.commons.util.DataUtil;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.*;
import com.trasen.tsproject.model.*;
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

    @Autowired
    TbProjectPlanLogMapper tbProjectPlanLogMapper;

    @Autowired
    TbPlanCheckMapper tbPlanCheckMapper;

    @Autowired
    OutputValueService outputValueService;

    public TbPlanDetail getPlanItemList(Integer planId, String type){
        TbPlanDetail detail = null;
        if(planId!=null){
            TbProjectPlan projectPlan = tbPlanDetailMapper.getProjectPlan(planId);
            if(projectPlan!=null&&projectPlan.getProCode()!=null&&projectPlan.getPlanId()!=null&&type!=null){
                List<TbPlanItem> list = new ArrayList<>();
                detail = tbPlanDetailMapper.getPlanDetail(projectPlan.getPlanId());
                if(detail==null){
                    detail = new TbPlanDetail();
                    detail.setPlanId(projectPlan.getPlanId());
                    detail.setOperator(VisitInfoHolder.getShowName());
                    tbPlanDetailMapper.insertPlanDetail(detail);
                }else{
                    Map<String,Object> itemMap = new HashMap<>();
                    itemMap.put("planId",detail.getPlanId());
                    itemMap.put("detailId",detail.getPkid());
                    list = tbPlanItemMapper.queryPlanItems(itemMap);
                }

                //角色权限

                String userRole = tbPlanItemMapper.getUserRoleTag(VisitInfoHolder.getUserId());
                detail.setUserRole(userRole);



                if(list.size()==0){
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
                }

                String checkRole = "";

                for(TbPlanItem planItem: list){
                    String role = planItem.getRole();
                    if(role!=null&&checkRole.length()<role.length()){
                        checkRole = role;
                    }
                    List<TbPlanCheck> planChecks = tbPlanCheckMapper.queryPlanCheck(planItem);
                    if(planChecks.size()>0){
                        planItem.setPlanChecks(planChecks);
                    }else{
                        planChecks = new ArrayList<>();
                        planItem.setPlanChecks(planChecks);
                        if(role!=null){
                            String [] tags = role.split("\\|");
                            if(tags!=null&&tags.length>1){
                                for(String tagId : tags){
                                    TwfCheckTag tag = tbPlanCheckMapper.getCheckTag("|"+tagId+"|");
                                    if(tag==null){
                                        continue;
                                    }
                                    TbPlanCheck check = new TbPlanCheck();
                                    check.setPlanId(planItem.getPlanId());
                                    check.setDetailId(planItem.getDetailId());
                                    check.setItemId(planItem.getPkid());
                                    check.setCheckTag(tag.getTagId());
                                    check.setCheckName(tag.getTagName());
                                    check.setPermission(tag.getPermission());
                                    check.setOperator(VisitInfoHolder.getShowName());
                                    check.setStatus(0);
                                    tbPlanCheckMapper.insertPlanCheck(check);
                                    planItem.getPlanChecks().add(check);
                                }
                            }
                        }
                    }

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
                            stage.setPlanStartTime(planItem.getPlanTime());
                            stage.setPlanEndTime(planItem.getPlanTime());
                        }
                        stageMap.put(planItem.getStageName(),stage);
                    }else{
                        stage = stageMap.get(planItem.getStageName());
                        stage.getTbPlanItems().add(planItem);
                        if(planItem.getPlanTime()!=null){
                            if(stage.getPlanStartTime()!=null){
                                if(DateUtils.strToDate(stage.getPlanStartTime(),"yyyy-MM-dd").getTime()>DateUtils.strToDate(planItem.getPlanTime(),"yyyy-MM-dd").getTime()){
                                    stage.setPlanStartTime(planItem.getPlanTime());
                                }
                            }else{
                                stage.setPlanStartTime(planItem.getPlanTime());
                            }
                            if(stage.getPlanEndTime()!=null){
                                if(DateUtils.strToDate(stage.getPlanEndTime(),"yyyy-MM-dd").getTime()<DateUtils.strToDate(planItem.getPlanTime(),"yyyy-MM-dd").getTime()){
                                    stage.setPlanEndTime(planItem.getPlanTime());
                                }
                            }else{
                                stage.setPlanEndTime(planItem.getPlanTime());
                            }
                        }
                    }
                }
                List<TbPlanStage> stageList = new ArrayList<>();
                stageList.addAll(stageMap.values());
                detail.setTbPlanStages(stageList);
                detail.setCheckRole(checkRole);
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

    public void updatePlanItemTime(List<TbPlanItem> planItemList){
        for(TbPlanItem planItem : planItemList){
            if(planItem.getPlanTime()!=null){
                planItem.setOperator(VisitInfoHolder.getShowName());
                planItem.setIsUpdate(0);
                tbPlanItemMapper.updatePlanItemTime(planItem);
            }
        }
    }

    public TbPlanItem updatePlanTime(TbPlanItem planItem){
        if(planItem!=null&&planItem.getPkid()!=null){
            boolean upBoo = false;
            TbPlanItem item = tbPlanItemMapper.getPlanItem(planItem.getPkid());
            if(item.getPlanTime()!=null&&planItem.getPlanTime()!=null
                    &&!item.getPlanTime().equals(planItem.getPlanTime())){
                TbProjectPlanLog log = new TbProjectPlanLog();
                log.setType("actualizePlan");
                log.setPlanId(planItem.getPkid());
                log.setOldTime(item.getPlanTime());
                log.setNewTime(planItem.getPlanTime());
                log.setCode("doc");// TODO: 17/11/2 类型写入常量类
                log.setRemark(planItem.getRemark());
                log.setOperator(VisitInfoHolder.getShowName());
                tbProjectPlanLogMapper.insertPlanLog(log);
                planItem.setIsUpdate(1);
                upBoo = true;
            }
            if(upBoo){
                planItem.setOperator(VisitInfoHolder.getShowName());
                tbPlanItemMapper.updatePlanItemTime(planItem);
            }

        }
        return planItem;

    }


    public List<TbProjectPlanLog> queryPlonTimechangeLog(String code,Integer planId,String type){
        List<TbProjectPlanLog> list = new ArrayList<>();
        if(code!=null&&planId!=null){
            Map<String,Object> param = new HashMap<>();
            param.put("code",code);
            param.put("planId",planId);
            param.put("type",type);
            list = tbProjectPlanLogMapper.queryPlonTimechangeLog(param);

        }
        return list;
    }

    public void updatePlanItemDocFile(TbPlanItem tbPlanItem){
        tbPlanItemMapper.updatePlanItemDocFile(tbPlanItem);
        tbPlanItemMapper.reUpload(tbPlanItem.getPkid());
    }

    public boolean checkOk(TbPlanItem item){
        boolean boo = false;
        if(item!=null&&item.getPkid()!=null&&item.getUserRole()!=null){
            tbPlanItemMapper.updateItemScore(item);
            Map<String,Object> param = new HashMap<>();
            param.put("status",1);
            param.put("operator",VisitInfoHolder.getShowName());
            param.put("itemId",item.getPkid());
            param.put("checkTag",item.getUserRole());
            tbPlanItemMapper.updateCheck(param);
            if("|tag_check_XMZ|".equals(item.getUserRole())){
                tbPlanItemMapper.updateItemComplete(item);
                //自动加入待确认产值
                outputValueService.addOutputValue(item);

                // TODO: 17/12/6 更新进度
            }
            boo = true;
        }
        return boo;
    }

    public boolean checkBack(TbPlanItem item){
        boolean boo = false;
        if(item!=null&&item.getPkid()!=null&&item.getUserRole()!=null){
            tbPlanItemMapper.planItemDocBack(item);
            Map<String,Object> param = new HashMap<>();
            param.put("status",2);
            param.put("operator",VisitInfoHolder.getShowName());
            param.put("itemId",item.getPkid());
            param.put("checkTag",item.getUserRole());
            tbPlanItemMapper.updateCheck(param);
            boo = true;
        }
        return boo;
    }









}
