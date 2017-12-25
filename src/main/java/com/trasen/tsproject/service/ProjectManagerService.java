package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbProjectManagerMapper;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbProjectManager;
import com.trasen.tsproject.model.TbProjectPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/12/25
 */
@Service
public class ProjectManagerService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectManagerService.class);

    @Autowired
    TbProjectManagerMapper tbProjectManagerMapper;

    public PageInfo<TbProjectManager> selectProjectManagerList(int page, int rows, TbProjectManager tbProjectManager){
        PageHelper.startPage(page,rows);
        List<TbProjectManager> tbProjectManagers=tbProjectManagerMapper.selectProjectManagerList(tbProjectManager);
        PageInfo<TbProjectManager> pagehelper = new PageInfo<TbProjectManager>(tbProjectManagers);
        return pagehelper;
    }

    public boolean deleteManager(Integer pkid){
        boolean boo=false;
        tbProjectManagerMapper.deleteManager(pkid);
        boo=true;
        return boo;
    }

    public int selectCount(TbProjectManager tbProjectManager){
        return tbProjectManagerMapper.selectCount(tbProjectManager);
    }

    @Transactional(rollbackFor = Exception.class)
    public int saveProjectManager(TbProjectManager tbProjectManager){
        tbProjectManager.setCreated(new Date());
        return tbProjectManagerMapper.saveProjectManager(tbProjectManager);
    }

    public int updateProjectManager(TbProjectManager tbProjectManager){
        return tbProjectManagerMapper.updateProjectManager(tbProjectManager);
    }
}
