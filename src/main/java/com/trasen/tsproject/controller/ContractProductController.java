package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.model.TbHtResolve;
import com.trasen.tsproject.service.ContractProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    @RequestMapping(value="/queryHtResolve",method = RequestMethod.POST)
    public Result queryHtResolve(@RequestBody Map<String,String> param){
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(param==null){
                result.setMessage("参数错误");
                return result;
            }
            if(StringUtil.isEmpty(param.get("contractNo"))){
                result.setMessage("合同号为空");
                return result;
            }
            if(StringUtil.isEmpty(param.get("contrachospitalLeveltNo"))){
                param.put("hospitalLevel","0");
            }
            if(StringUtil.isEmpty(param.get("contractPrice"))){
                result.setMessage("合同金额为空，请查看数据是否正确");
                return result;
            }

            List<TbHtResolve> list = contractProductService.queryHtResolve("B15075");
            result.setSuccess(true);
            result.setObject(list);
        }catch (Exception e) {
            logger.error("获取合同分解异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取合同分解失败");
        }
        return  result;
    }

    @RequestMapping(value="/updateModulePrice",method = RequestMethod.POST)
    public Result updateModulePrice(@RequestBody List<TbHtModule> htModuleList) {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(htModuleList==null||htModuleList.size()==0){
                result.setMessage("参数错误");
                return result;
            }
            boolean boo = contractProductService.updateModulePrice(htModuleList);
            List<TbHtResolve> resolveList = contractProductService.queryHtResolve(htModuleList.get(1).getHtNo());
            result.setSuccess(boo);
            result.setObject(resolveList);
        }catch (Exception e) {
            logger.error("修改合同模块价格异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("修改合同模块价格失败");
        }
        return  result;
    }


    /**
     * 查询合同模块
     * */
    @RequestMapping(value="/queryHtModule", method = RequestMethod.POST)
    public Result queryHtModule(@RequestBody TbHtResolve htResolve)  {
        Result result=new Result();
        result.setSuccess(false);
        try {
            if(htResolve==null){
                result.setMessage("参数对象错误");
                return result;
            }
            if(htResolve.getHtNo()==null||htResolve.getProCode()==null){
                result.setMessage("合同号或者产品code为空");
                return result;
            }
            List<TbHtModule> list = contractProductService.queryHtModule(htResolve);
            result.setObject(list);
            result.setSuccess(true);
        }catch (Exception e) {
            logger.error("查询合同模块异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查询合同模块失败");
        }
        return  result;
    }







}
