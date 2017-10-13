package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.util.MyMapper;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/13
 */
public interface TbPersonnelMapper extends MyMapper<TbPersonnel> {
    TbPersonnel selectTbPersonnel(String userId);
}
