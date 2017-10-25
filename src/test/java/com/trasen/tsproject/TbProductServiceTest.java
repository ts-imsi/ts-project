package com.trasen.tsproject;

import com.trasen.tsproject.model.TbProModule;
import com.trasen.tsproject.service.TbProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= Application.class)
@Transactional
public class TbProductServiceTest {
    @Autowired
    private TbProductService tbProductService;

    @Test
    @Rollback(true)
    public void saveTbProductModule(){
        List<String> tbProModuleList=new ArrayList<>();

        tbProModuleList.add("12187");
        tbProModuleList.add("12834");
        tbProductService.saveTbProductModule("B15073","2.8000","0",tbProModuleList);
    }
}
