package com.transen.tsproject.controller;

import com.transen.tsproject.service.ContractProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@RestController
@RequestMapping(value="/ts-project/con_product")
public class ContractProductController {
    private static final Logger logger = Logger.getLogger(ContractProductController.class);

    @Autowired
    private ContractProductService contractProductService;

    @RequestMapping(value="/getcontractTransenList",method = RequestMethod.POST)
    public Map<String,Object> getcontractTransenList(@RequestBody Map<String,String> param){
        return contractProductService.getcontractTransenList(param);
    }
}
