package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbPlanTemplateMapper;
import com.trasen.tsproject.dao.TwfStageMapper;
import com.trasen.tsproject.model.TbPlanTemplate;
import com.trasen.tsproject.model.TwfStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    TwfStageMapper twfStageMapper;


    public PageInfo<TbPlanTemplate> queryPlanTemp(int page, int rows){
        PageHelper.startPage(page,rows);
        List<TbPlanTemplate> tbPlanTemplateList=tbPlanTemplateMapper.queryPlanTemp();
        PageInfo<TbPlanTemplate> pagehelper = new PageInfo<TbPlanTemplate>(tbPlanTemplateList);
        return pagehelper;
    }

    public List<TwfStage> selectTwfStageList(){
        return twfStageMapper.selectTwfStageList();
    }
}
