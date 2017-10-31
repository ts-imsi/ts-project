package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbHtHandoverMapper;
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
 * @date 2017/10/30
 */
@Service
public class ProjectArrangeService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectArrangeService.class);

    @Autowired
    private TbHtHandoverMapper tbHtHandoverMapper;

    public PageInfo<TbHtHandover> selectProjectArrangeList(int page,int rows,TbHtHandover tbHtHandover){
        PageHelper.startPage(page,rows);
        List<TbHtHandover> tbHtHandoverList=tbHtHandoverMapper.selectProjectArrangeList(tbHtHandover);
        PageInfo<TbHtHandover> pagehelper = new PageInfo<TbHtHandover>(tbHtHandoverList);
        return pagehelper;
    }

}
