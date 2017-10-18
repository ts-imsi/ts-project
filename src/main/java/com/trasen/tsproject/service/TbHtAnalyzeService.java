package com.trasen.tsproject.service;

import com.trasen.tsproject.dao.TbHtAnalyzeMapper;
import com.trasen.tsproject.dao.TbPersonnelMapper;
import com.trasen.tsproject.model.TbHtAnalyze;
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

    public List<TbHtAnalyze> selectAnalyzeList(String htNo){
        List<TbHtAnalyze> tbHtAnalyzeList= tbHtAnalyzeMapper.selectAnalyzeList(htNo);
        for(TbHtAnalyze tbHtAnalyze:tbHtAnalyzeList){
            tbHtAnalyze.setData(tbPersonnelMapper.selectTbPersonnelList(tbHtAnalyze.getDepId()));
        }
        return tbHtAnalyzeList;
    }

    @Transactional(rollbackFor=Exception.class)
    public boolean saveAnaly(List<TbHtAnalyze> tbHtAnalyzes){
        boolean boo=false;
        tbHtAnalyzeMapper.deleteAnaly(tbHtAnalyzes.get(0).getHtNo());
        for(TbHtAnalyze tbHtAnalyze:tbHtAnalyzes){
            String[] array=tbHtAnalyze.getPersonJson().split(",");
            for(int i=0;i<array.length;i++){
                tbHtAnalyze.setOperator(array[i]);
                tbHtAnalyze.setStatus(0);
                tbHtAnalyzeMapper.saveAnaly(tbHtAnalyze);
            }
        }
        boo=true;
        return boo;
    }
}
