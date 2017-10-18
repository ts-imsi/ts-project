package com.trasen.tsproject.service;

import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbPersonnelMapper;
import com.trasen.tsproject.model.Select;
import com.trasen.tsproject.model.TbPersonnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        TbPersonnel tbPersonnel=tbPersonnelMapper.selectTbPersonnel("3");
        return tbPersonnel;
    }

    public List<Select> selectTbPersonnel(String depId){
        return tbPersonnelMapper.selectTbPersonnelList(depId);
    }


}
