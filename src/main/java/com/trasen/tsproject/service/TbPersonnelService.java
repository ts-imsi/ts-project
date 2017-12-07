package com.trasen.tsproject.service;

import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbPersonnelMapper;
import com.trasen.tsproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/13
 */
@Service
public class TbPersonnelService {

    @Autowired
    private TbPersonnelMapper tbPersonnelMapper;

    public TbPersonnel selectTbPersonnel(){
        //TODO VisitInfoHolder.getUserId() 暂时设置
        TbPersonnel tbPersonnel=tbPersonnelMapper.selectTbPersonnel(VisitInfoHolder.getUserId());
        return tbPersonnel;
    }

    public List<Select> selectTbPersonnel(String depId){
        return tbPersonnelMapper.selectTbPersonnelList(depId);
    }

    public TbUser selectTbuserByOpenId(String openId){
        return tbPersonnelMapper.selectTbuserByOpenId(openId);
    }
    public TreeVo getDeptTree(TbTree tbTree) {
        TreeVo vo = new TreeVo();
        if (tbTree!=null) {
            vo.setLabel(tbTree.getName());
            vo.setData(tbTree);
            List<TbTree> treeList = tbPersonnelMapper.getDeptTreeList(tbTree.getPkid());
            List<TreeVo> children = new ArrayList<>();
            for (TbTree tree : treeList) {
                TreeVo childrenVo = getDeptTree(tree);
                children.add(childrenVo);
            }
            vo.setChildren(children);
        }
        return vo;
    }

    public TbTree getParentTree(){
        return tbPersonnelMapper.getParentTree();
    }

    public List<TwfDict> selectTwfDictByType(String type){
        return tbPersonnelMapper.selectTwfDictByType(type);
    }

}
