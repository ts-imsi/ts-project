package com.trasen.tsproject.service;

import com.trasen.tsproject.dao.TbHtModuleChangeMapper;
import com.trasen.tsproject.model.TbHtModuleChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/27
 */
@Service
public class TbHtModuleChangeService {

    @Autowired
    TbHtModuleChangeMapper tbHtModuleChangeMapper;

    @Transactional(rollbackFor = Exception.class)
    public int saveHtModuleChange(TbHtModuleChange tbHtModuleChange){
        return tbHtModuleChangeMapper.saveHtModuleChange(tbHtModuleChange);
    }
}
