package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbHtChangeMapper;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbHtHandover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/12
 */
@Service
public class TbHtChangeService {

    private static final Logger logger = LoggerFactory.getLogger(TbHtChangeService.class);

    @Autowired
    private TbHtChangeMapper tbHtChangeMapper;

    public PageInfo<TbHtChange> getHtChangeList(int page,int rows,TbHtChange tbHtChange){
        PageHelper.startPage(page,rows);
        List<TbHtChange> tbHtChangeList=tbHtChangeMapper.getHtChangeList(tbHtChange);
        PageInfo<TbHtChange> pagehelper = new PageInfo<TbHtChange>(tbHtChangeList);
        return pagehelper;
    }

    public boolean applySubmit(TbHtChange tbHtChange){
        boolean boo=false;
        tbHtChangeMapper.saveHtChange(tbHtChange);
        logger.info("合同变更，合同变更id"+tbHtChange.getPkid());
        //TODO 启动流程
        boo=true;
        return boo;
    }


}
