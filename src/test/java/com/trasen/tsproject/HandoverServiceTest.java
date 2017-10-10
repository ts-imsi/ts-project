package com.trasen.tsproject;

import com.alibaba.fastjson.JSON;
import com.trasen.tsproject.model.TempDataVo;
import com.trasen.tsproject.service.HandoverService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangxiahui on 17/10/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= Application.class)
@Transactional
public class HandoverServiceTest {

    @Autowired
    HandoverService handoverService;

    @Test
    public void getTempDataList(){
        List<TempDataVo> list = handoverService.getTempDataList(1);
        String str = JSON.toJSONString(list);
        System.out.println(str);
        for(TempDataVo vo : list){
            System.out.println(vo.getName()+"=="+vo.getValue());
        }
    }
}
