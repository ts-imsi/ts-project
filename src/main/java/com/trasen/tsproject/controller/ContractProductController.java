package com.trasen.tsproject.controller;

import com.trasen.tsproject.service.ContractProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@RestController
@RequestMapping(value="/con_product")
public class ContractProductController {
    private static final Logger logger = Logger.getLogger(ContractProductController.class);

    @Autowired
    private ContractProductService contractProductService;

    @RequestMapping(value="/getcontractTransenList",method = RequestMethod.POST)
    public Map<String,Object> getcontractTransenList(@RequestBody Map<String,String> param){
        return contractProductService.getcontractTransenList(param);
    }

/*    @RequestMapping(value="/getProductByContract",method = RequestMethod.POST)
    public Map<String,Object> getProductByContract(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        if(param.get("contractNo")==null||param.get("contractNo")==""){
            paramMap.put("message","合同号为空");
            paramMap.put("succes",false);
            return paramMap;
        }
        if(param.get("hospitalLevel")==null||param.get("hospitalLevel")==""){
            param.put("hospitalLevel","0");
        }
        if(param.get("contractPrice")==null||param.get("contractPrice")==""){
            paramMap.put("message","合同金额为空，请查看数据是否正确");
            paramMap.put("succes",false);
            return paramMap;
        }
        Map<String,Object> result=contractProductService.getProductByContract(param.get("contractNo"),param.get("hospitalLevel"),Double.valueOf(param.get("contractPrice")));
        return result;
    }

    @RequestMapping(value="/updateProductByContract",method = RequestMethod.POST)
    public Map<String,Object> updateProductByContract(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        if(param.get("contractNo")==null||param.get("contractNo")==""){
            paramMap.put("message","合同号为空");
            paramMap.put("succes",false);
            return paramMap;
        }
        if(param.get("pkid")==null||param.get("pkid")==""){
            paramMap.put("message","参数为空");
            paramMap.put("succes",false);
            return paramMap;
        }
        if(param.get("contractPrice")==null||param.get("contractPrice")==""){
            paramMap.put("message","合同金额为空，请查看数据是否正确");
            paramMap.put("succes",false);
            return paramMap;
        }
        if(param.get("standardPrice")==null||param.get("standardPrice")==""){
            paramMap.put("message","修改的标准价为空");
            paramMap.put("succes",false);
            return paramMap;
        }
        Map<String,Object> result=contractProductService.updateProductByContract(param.get("contractNo"),Integer.valueOf(param.get("pkid")),Double.valueOf(param.get("contractPrice")),Double.valueOf(param.get("standardPrice")));
        return result;
    }*/
}
