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
import java.util.Map;

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
        Map<String,Object> map = handoverService.getTempDataList(5);
        String str = JSON.toJSONString(map);
        System.out.println(str);

    }
}
