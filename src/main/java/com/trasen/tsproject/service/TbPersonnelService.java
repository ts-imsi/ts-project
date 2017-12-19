package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.controller.OutputValueController;
import com.trasen.tsproject.dao.TbPersonnelMapper;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.SignConvertUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private Environment env;

    private static final Logger logger = Logger.getLogger(TbPersonnelService.class);

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

    public TbUser ctreateXToken(TbUser tbUser){
        Map<String, String> parameters = new HashedMap();
        parameters.put("name", tbUser.getName());
        parameters.put("pwd", tbUser.getPassword());
        parameters.put("showName", tbUser.getDisplayName());
        parameters.put("userId", tbUser.getPkid().toString());
        try {
            String secret = env.getProperty("CONTENT_SECRET");
            String sign = SignConvertUtil.generateMD5Sign(secret, parameters);
            String parameterJson = JSONObject.toJSONString(parameters);
            String asB64 = Base64.getEncoder().encodeToString(parameterJson.getBytes("utf-8"));
            String xtoken = sign+"."+asB64;
            tbUser.setXtoken(xtoken);
        } catch(NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch(UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return tbUser;
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
