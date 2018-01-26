package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TwfDictMapper;
import com.trasen.tsproject.model.TwfDict;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/26
 */
@Service
public class TwfDictService {

    private static final Logger logger=Logger.getLogger(TwfDictService.class);

    @Autowired
    TwfDictMapper twfDictMapper;

    public int deleteTwfDict(Integer pkid){
        return twfDictMapper.deleteTwfDict(pkid);
    }

    public PageInfo<TwfDict> selectTwfDictByType(int page, int rows,TwfDict twfDict){
        PageHelper.startPage(page,rows);
        List<TwfDict> tbPlanTemplateList=twfDictMapper.selectTwfDictByType(twfDict);
        PageInfo<TwfDict> pagehelper = new PageInfo<TwfDict>(tbPlanTemplateList);
        return pagehelper;
    }

    public int saveTwfDict(TwfDict twfDict){
        return twfDictMapper.saveTwfDict(twfDict);
    }

    public int updateTwfDict(TwfDict twfDict){
        return twfDictMapper.updateTwfDict(twfDict);
    }


}
