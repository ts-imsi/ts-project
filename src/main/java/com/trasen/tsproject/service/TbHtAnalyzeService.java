package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtAnalyzeMapper;
import com.trasen.tsproject.dao.TbHtResolveMapper;
import com.trasen.tsproject.dao.TbPersonnelMapper;
import com.trasen.tsproject.model.Select;
import com.trasen.tsproject.model.TbHtAnalyze;
import com.trasen.tsproject.model.TbHtResolve;
import com.trasen.tsproject.model.TbPersonnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/17
 */
@Service
public class TbHtAnalyzeService {

    @Autowired
    private TbHtAnalyzeMapper tbHtAnalyzeMapper;

    @Autowired
    private TbPersonnelMapper tbPersonnelMapper;

    @Autowired
    private TbHtResolveMapper tbHtResolveMapper;


    public List<TbHtAnalyze> selectAnalyzeList(String htNo){
        List<TbHtAnalyze> tbHtAnalyzeList= tbHtAnalyzeMapper.selectAnalyzeList(htNo);
        for(TbHtAnalyze tbHtAnalyze:tbHtAnalyzeList){
            tbHtAnalyze.setData(tbPersonnelMapper.selectTbPersonnelList(tbHtAnalyze.getDepId()));
            List<Select> selectList=tbHtAnalyzeMapper.getSelectJson(tbHtAnalyze);
            if(selectList!=null&&selectList.size()>0){
                tbHtAnalyze.setSelectJson(selectList);
            }
        }
        return tbHtAnalyzeList;
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean saveAnaly(List<TbHtAnalyze> tbHtAnalyzes){
        boolean boo=false;
        //删除数据
        tbHtAnalyzeMapper.deleteAnaly(tbHtAnalyzes.get(0).getHtNo());
        //保存数据
        for(TbHtAnalyze tbHtAnalyze:tbHtAnalyzes){
            List<Select> selectList=tbHtAnalyze.getSelectJson();
            for(Select select:selectList){
                tbHtAnalyze.setOperator(select.getId());
                tbHtAnalyze.setStatus(0);
                tbHtAnalyzeMapper.saveAnaly(tbHtAnalyze);
            }
        }
        //更新分解表分解人数据
        TbHtResolve tbHtResolve=new TbHtResolve();
        tbHtResolve.setHtNo(tbHtAnalyzes.get(0).getHtNo());
        TbPersonnel tbPersonnel=tbPersonnelMapper.selectTbPersonnel(VisitInfoHolder.getUserId());
        if(tbPersonnel!=null){
            tbHtResolve.setProMan(tbPersonnel.getWorkNum());
        }else{
            tbHtResolve.setProMan(VisitInfoHolder.getUserId());
        }
        tbHtResolveMapper.updateProMan(tbHtResolve);
        boo=true;
        return boo;
    }
}
