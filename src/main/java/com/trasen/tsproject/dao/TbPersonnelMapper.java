package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/13
 */
public interface TbPersonnelMapper extends MyMapper<TbPersonnel> {
    TbPersonnel selectTbPersonnel(String userId);
    List<Select> selectTbPersonnelList(String depId);
    TbUser selectTbuserByOpenId(String openId);
    TbTree getParentTree();
    List<TbTree> getDeptTreeList(String pkid);
    List<TwfDict> selectTwfDictByType(String type);

    TbPersonnel weixinToPersonnel(String openId);

}
