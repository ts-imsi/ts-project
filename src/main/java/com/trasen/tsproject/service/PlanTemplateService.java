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
        List<String> checkTagSaveList=tagSaveList.stream().sorted().collect(Collectors.toList());
        List<String> checkStageSaveList=stageSaveList.stream().sorted().collect(Collectors.toList());
        List<TwfCheckTag> twfCheckTags=twfCheckTagMapper.selectCheckTag();
        checkStageSaveList.stream().forEach(stage ->savaPlanTemplateItem(tbPlanTemplate,checkTagSaveList,stage,twfCheckTags));
    }
    public void savaPlanTemplateItem(TbPlanTemplate tbPlanTemplate,List<String> tagSaveList,String stage,List<TwfCheckTag> twfCheckTags){
        String[] stages=stage.split(":");

        List<String> tagName=tagSaveList.stream().map(tag->analysisPlan(tag,stages,twfCheckTags)).collect(Collectors.toList());
        String tagId=String.join("",tagName);
        TbPlanTemplateItem tbPlanTemplateItem=new TbPlanTemplateItem();
        tbPlanTemplateItem.setTempId(tbPlanTemplate.getPkid());
        tbPlanTemplateItem.setStageDocId(Integer.valueOf(stages[1]));
        tbPlanTemplateItem.setStageId(Integer.valueOf(stages[0]));
        tbPlanTemplateItem.setOperator(VisitInfoHolder.getShowName());
        tbPlanTemplateItem.setCreated(new Date());
        String role=tagId.replace("||","|");
        tbPlanTemplateItem.setRole(role);
        tbPlanTemplateItemMapper.saveTemplateItem(tbPlanTemplateItem);
    }

    public String analysisPlan(String tag,String[] stages,List<TwfCheckTag> twfCheckTags){
        String[] sz=tag.split(":");
        String tagName="";
        if(stages[1].equals(sz[0])){
            List<String> check=twfCheckTags.stream().filter(checkTag->checkTag.getPkid()==Integer.valueOf(sz[1])).map(n->n.getTagId()).collect(Collectors.toList());
            tagName+=check.get(0);
        }
        System.out.println("=============="+tagName);
        logger.info("=============="+tagName);
        return tagName;
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
        List<String> stagePlan=tbPlanTemplateItems.stream().map(item->item.getStageId()+":"+item.getStageDocId()).distinct().collect(Collectors.toList());

        List<String> tagPlan=tbPlanTemplateItems.stream().map(planTag->planTag.getStageDocId()+":"+planTag.getCheckTagId()).distinct().collect(Collectors.toList());

        List<Integer> stageList=tbPlanTemplateItems.stream().map(stage->stage.getStageId()).distinct().collect(Collectors.toList());
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
