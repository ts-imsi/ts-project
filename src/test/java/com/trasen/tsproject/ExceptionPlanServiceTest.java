package com.trasen.tsproject;

import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.service.ExceptionPlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= Application.class)
@Transactional
public class ExceptionPlanServiceTest {
    @Autowired
    ExceptionPlanService exceptionPlanService;

    @Test
    public void selectExceptionPlan(){
        Map<String,String> param=new HashMap<>();
        param.put("rows","10");
        param.put("page","1");
        param.put("status","0");

        /*PageInfo<ExceptionPlan> exceptionPlanPageInfo=exceptionPlanService.selectExceptionPlan(param);
        exceptionPlanPageInfo.getList().stream().forEach(exce->System.out.println(exce.getCheckName()));*/
    }

}
