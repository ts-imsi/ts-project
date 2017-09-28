package com.trasen.tsproject.service;

import cn.trasen.commons.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbTemplateItemMapper;
import com.trasen.tsproject.dao.TbTemplateMapper;
import com.trasen.tsproject.model.TbTemplate;
import com.trasen.tsproject.model.TbTemplateItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiahui on 17/9/26.
 */
@Service
public class TemplateService {

    Logger logger = Logger.getLogger(TemplateService.class);

    @Autowired
    TbTemplateItemMapper tbTemplateItemMapper;

    @Autowired
    TbTemplateMapper templateMapper;

    public List<TbTemplateItem> queryItemList(String type){
        List<TbTemplateItem> list = new ArrayList<>();
        if(!StringUtil.isEmpty(type)){
            list = tbTemplateItemMapper.queryItemList(type);
        }
        return list;
    }

    public TbTemplate getTemplate(String type){
        TbTemplate tbTemplate = new TbTemplate();
        if(!StringUtil.isEmpty(type)){
            tbTemplate = templateMapper.getTemplate(type);
            if(tbTemplate.getContent()!=null){
                List<TbTemplateItem> list = JSON.parseArray(tbTemplate.getContent(), TbTemplateItem.class);
                tbTemplate.setContentJson(list);
            }
        }
        return tbTemplate;
    }

    public PageInfo<TbTemplate> getTemplateList(int page, int rows, TbTemplate tbTemplate){
        PageHelper.startPage(page,rows);
        List<TbTemplate> tbTemplateList=templateMapper.getTemplateList(tbTemplate);
        PageInfo<TbTemplate> pagehelper = new PageInfo<TbTemplate>(tbTemplateList);
        return pagehelper;
    }

    public boolean saveTemplate(TbTemplate tbTemplate){
        boolean boo = false;
        if(tbTemplate.getPkid()!=null){
            List<TbTemplateItem> list = tbTemplate.getContentJson();
            String content = JSON.toJSONString(list);
            tbTemplate.setContent(content);
            tbTemplate.setOperator(VisitInfoHolder.getUserId());
            templateMapper.updateTemplate(tbTemplate);
            templateMapper.insertTemplate(tbTemplate);
            boo = true;
        }
        return boo;
    }


}
