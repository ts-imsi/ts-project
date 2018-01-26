package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TwfDict;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/26
 */
public interface TwfDictMapper extends MyMapper<TwfDict> {

    List<TwfDict> selectTwfDictByType(TwfDict twfDict);
    int saveTwfDict(TwfDict twfDict);
    int deleteTwfDict(Integer pkid);
    int updateTwfDict(TwfDict twfDict);

}
