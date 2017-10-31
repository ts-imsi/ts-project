package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbProjectManager;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbProjectManagerMapper extends MyMapper<TbProjectManager> {
    List<TbProjectManager> getManageByType(String type);
}