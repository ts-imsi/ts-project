package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProjectManager;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbProjectManagerMapper extends MyMapper<TbProjectManager> {
    List<TbProjectManager> getManageByType(String type);
    List<TbProjectManager> selectProjectManagerList(TbProjectManager tbProjectManager);
    int selectCount(TbProjectManager tbProjectManager);
    int deleteManager(Integer pkid);
    int saveProjectManager(TbProjectManager tbProjectManager);
    int updateProjectManager(TbProjectManager tbProjectManager);
}