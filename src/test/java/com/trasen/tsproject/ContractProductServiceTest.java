package com.trasen.tsproject;


import com.trasen.tsproject.model.TbHtProduct;
import com.trasen.tsproject.service.ContractProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= Application.class)
@Transactional
public class ContractProductServiceTest {

    @Autowired
    private ContractProductService contractProductService;

    @Test
    public void getcontractTransenList(){
        Map<String,String> param=new HashMap<String,String>();
        param.put("page","1");
        param.put("rows","10");
        Map<String,Object> paramMap= contractProductService.getcontractTransenList(param);

        System.out.println(paramMap.get("totalPages"));
    }

    @Test
    @Rollback(false)
    public void getProductByContract(){
        Map<String,Object> param=contractProductService.getProductByContract("B16080","0",12.38);
        List<TbHtProduct> tbHtProductList= (List<TbHtProduct>) param.get("list");
        for(TbHtProduct tbHtProduct:tbHtProductList){
            System.out.println(tbHtProduct.getHtNo()+","+tbHtProduct.getProductName()+tbHtProduct.getSubtotal()+","+tbHtProduct.getOutputValue());
        }


    }
}
