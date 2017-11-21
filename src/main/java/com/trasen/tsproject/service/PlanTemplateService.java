package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.*;
import com.trasen.tsproject.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/15
 */
@Service
public class PlanTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(PlanTemplateService.class);

    @Autowired
    TbPlanTemplateMapper tbPlanTemplateMapper;

    @Autowired
    TbPlanTemplateItemMapper tbPlanTemplateItemMapper;

    @Autowired
    TwfStageMapper twfStageMapper;

    @Autowired
    TwfStageDocMapper twfStageDocMapper;

    @Autowired
    TwfCheckTagMapper twfCheckTagMapper;


    public PageInfo<TbPlanTemplate> queryPlanTemp(int page, int rows){
        PageHelper.startPage(page,rows);
        List<TbPlanTemplate> tbPlanTemplateList=tbPlanTemplateMapper.queryPlanTemp();
        PageInfo<TbPlanTemplate> pagehelper = new PageInfo<TbPlanTemplate>(tbPlanTemplateList);
        return pagehelper;
    }

    public List<TwfStage> selectTwfStageList(){
        return twfStageMapper.selectTwfStageList();
    }

    public Map<String,Object> selectTwfStageTag(){
        Map<String,Object> param=new HashMap<>();
        param.put("twfStageList",twfStageMapper.selectTwfStageList());
        param.put("twfCheckTagList",twfCheckTagMapper.selectCheckTag());
        return param;
    }

    public List<TwfStageDoc> getTwfStageDocList(Integer stageId){
        return twfStageDocMapper.getTwfStageDocList(stageId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void savePlanTemp(TbPlanTemplate tbPlanTemplate,List<String> tagSaveList,List<String> stageSaveList){
        if(tbPlanTemplate.getPkid()==0){
            tbPlanTemplate.setOperator(VisitInfoHolder.getShowName());
            tbPlanTemplate.setCreated(new Date());
            tbPlanTemplateMapper.savePlanTemp(tbPlanTemplate);
        }else{
            tbPlanTemplate.setOperator(VisitInfoHolder.getShowName());
            tbPlanTemplate.setUpdated(new Date());
            tbPlanTemplateMapper.updatePlanTemp(tbPlanTemplate);
            tbPlanTemplateItemMapper.deletePlanItem(tbPlanTemplate.getPkid());
        }
        List<TwfCheckTag> twfCheckTags=twfCheckTagMapper.selectCheckTag();
        stageSaveList.stream().forEach(stage ->savaPlanTemplateItem(tbPlanTemplate,tagSaveList,stage,twfCheckTags));
    }
    public void savaPlanTemplateItem(TbPlanTemplate tbPlanTemplate,List<String> tagSaveList,String stage,List<TwfCheckTag> twfCheckTags){
        String[] stages=stage.split(":");
        tagSaveList.stream().forEach(tag->saveTemplateItem(tbPlanTemplate,tag,stages,twfCheckTags));
    }

    public void saveTemplateItem(TbPlanTemplate tbPlanTemplate,String tag,String[] stages,List<TwfCheckTag> twfCheckTags){
        String[] tags=tag.split(":");
        if(tags[0].equals(stages[1])){
            TbPlanTemplateItem tbPlanTemplateItem=new TbPlanTemplateItem();
            tbPlanTemplateItem.setTempId(tbPlanTemplate.getPkid());
            tbPlanTemplateItem.setStageDocId(Integer.valueOf(tags[0]));
            tbPlanTemplateItem.setStageId(Integer.valueOf(stages[0]));
            tbPlanTemplateItem.setOperator(VisitInfoHolder.getShowName());
            tbPlanTemplateItem.setCreated(new Date());
            List<TwfCheckTag> checkTags=twfCheckTags.stream().filter(checkTag->checkTag.getPkid()==Integer.valueOf(tags[1])).collect(Collectors.toList());
            tbPlanTemplateItem.setRole(checkTags.get(0).getTagId());
            tbPlanTemplateItemMapper.saveTemplateItem(tbPlanTemplateItem);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public int saveStageTemp(TwfStage twfStage){
        twfStage.setCreated(new Date());
        twfStage.setOperator(VisitInfoHolder.getShowName());
        return twfStageMapper.saveStageTemp(twfStage);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteStageTemp(int pkid){
        twfStageMapper.deleteStageTemp(pkid);
        return twfStageDocMapper.deleteDocByStageId(pkid);
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteDocByPkid(int pkid){
        return twfStageDocMapper.deleteDocByPkid(pkid);
    }

    @Transactional(rollbackFor = Exception.class)
    public int saveTwfStageDoc(TwfStageDoc twfStageDoc){
        twfStageDoc.setCreated(new Date());
        twfStageDoc.setOperator(VisitInfoHolder.getShowName());
        return twfStageDocMapper.saveTwfStageDoc(twfStageDoc);
    }

    public Map<String,Object> selectTempView(Integer pkid){
        Map<String,Object> param=new HashMap<>();
        List<TbPlanTemplateItem> tbPlanTemplateItems=tbPlanTemplateItemMapper.selectPlanItem(pkid);
        TbPlanTemplate tbPlanTemplate=tbPlanTemplateMapper.selectPlanTemp(pkid);
        List<String> stagePlan=tbPlanTemplateItems.stream().map(item->item.getStageId()+":"+item.getStageDocId()).collect(Collectors.toList());
        List<String> tagPlan=tbPlanTemplateItems.stream().map(planTag->planTag.getStageDocId()+":"+planTag.getCheckTagId()).collect(Collectors.toList());
        List<Integer> stageList=tbPlanTemplateItems.stream().map(stage->stage.getStageId()).collect(Collectors.toList());
        List<Integer> stageModuleList=stageList.stream().distinct().collect(Collectors.toList());
        param.put("tbPlanTemplate",tbPlanTemplate);
        param.put("stageSaveList",stagePlan);
        param.put("tagSaveList",tagPlan);
        param.put("stageModuleList",stageModuleList);
        return param;
    }

    public List<TwfCheckTag> querytwfCheckTagList(){
        return twfCheckTagMapper.selectCheckTag();
    }

}
