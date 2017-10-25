package com.trasen.tsproject;

import com.trasen.tsproject.service.TbMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/10/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= Application.class)
@Transactional
public class MsgServiceTest {

    @Autowired
    TbMsgService tbMsgService;

    @Test
    @Rollback(true)
    public void getTodoHandOver(){
        tbMsgService.synTodoHandOver("3","周林燕");
    }

    @Test
    @Rollback(true)
    public void selectTbMsg(){
        Map<String,String> param=new HashMap<String,String>();
        tbMsgService.selectTbMsg(1,10,param);
    }
}
