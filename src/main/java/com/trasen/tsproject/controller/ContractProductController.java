package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.model.TbHtResolve;
import com.trasen.tsproject.service.ContractProductService;
import com.trasen.tsproject.util.ArithmeticUtil;
import com.trasen.tsproject.util.JsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.ObjDoubleConsumer;

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
        Map<String,Object> paramMap=new HashMap<>();
        try{
            if(param.isEmpty()){
                paramMap.put("success",false);
                paramMap.put("message","参数参入错误");
            }
            if(param.get("showAll")==null||!"all".equals(param.get("showAll"))){
                //列表权限
                param.put("contractOwner", VisitInfoHolder.getShowName());
            }
            String status=Optional.ofNullable(param.get("status")).orElse("0");
            if(status.equals("0")){
                paramMap =contractProductService.getcontractTransenList(param);
            }else{
                List<ContractInfo> contractInfoList=contractProductService.getOaContractListByOwner(param);
                paramMap.put("list",contractInfoList);
                paramMap.put("totalPages",1);
                paramMap.put("pageNo",1);
                paramMap.put("totalCount",1);
                paramMap.put("pageSize",1);
                paramMap.put("success",true);
            }

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            paramMap.put("success",false);
            paramMap.put("message","数据查询失败");
        }
        return paramMap;
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
            if(StringUtil.isEmpty(param.get("hospitalLevel"))){
                param.put("hospitalLevel","0");
            }
            if(StringUtil.isEmpty(param.get("contractPrice"))){
                result.setMessage("合同金额为空，请查看数据是否正确");
                return result;
            }

            List<TbHtResolve> list = contractProductService.queryHtResolve(param.get("contractNo"));
            if(list==null||list.size()==0){
                //同步数据
                contractProductService.synchroHtModuleByContract(param.get("contractNo"),param.get("hospitalLevel"));
                //计算产值
                contractProductService.getOutputValueOrSubtotal(param.get("contractNo"),Double.parseDouble(param.get("contractPrice")));
                list = contractProductService.queryHtResolve(param.get("contractNo"));
            }
            Map<String,Object> sumMap=new HashMap<>();
            double suTotal=0.0;
            double xjTotal=0.0;
            double bfTotal=0.0;
            double priceTotal=0.0;
            for(TbHtResolve tbHtResolve:list){
                suTotal=ArithmeticUtil.add(suTotal,tbHtResolve.getTotal());
                xjTotal=ArithmeticUtil.add(xjTotal,tbHtResolve.getSubtotal());
                bfTotal=ArithmeticUtil.add(bfTotal,Double.valueOf(tbHtResolve.getOutputValue().substring(0,tbHtResolve.getOutputValue().length()-1)));
                priceTotal=ArithmeticUtil.add(priceTotal,tbHtResolve.getPrice());
            }
            result.setSuccess(true);
            sumMap.put("list",list);
            sumMap.put("suTotal",suTotal);
            sumMap.put("xjTotal",xjTotal);
            sumMap.put("bfTotal",bfTotal);
            sumMap.put("priceTotal",priceTotal);
            result.setObject(sumMap);
        }catch (Exception e) {
            logger.error("获取合同分解异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取合同分解失败");
        }
        return  result;
    }

    @RequestMapping(value="/updateModulePrice",method = RequestMethod.POST)
    public Result updateModulePrice(@RequestBody Map<String,Object> param) {
        Result result=new Result();
        result.setSuccess(false);
        try {

            if(param==null){
                result.setMessage("参数错误");
                return result;
            }

            if(param.get("htModuleList")==null){
                result.setMessage("模块数据为空，请查看数据是否正确");
                return result;
            }

            List<TbHtModule> htModuleList = JsonUtil.parseJsonList(JsonUtil.toJsonStr(param.get("htModuleList")),TbHtModule.class);

            if(htModuleList==null||htModuleList.size()==0){
                result.setMessage("参数错误");
                return result;
            }
            if(param.get("contractPrice")==null){
                result.setMessage("合同金额为空，请查看数据是否正确");
                return result;
            }

            boolean boo = contractProductService.updateModulePrice(htModuleList);
            if(boo){
                //计算产值
                contractProductService.getOutputValueOrSubtotal(htModuleList.get(0).getHtNo(),Double.parseDouble(param.get("contractPrice").toString()));
            }
            List<TbHtResolve> resolveList = contractProductService.queryHtResolve(htModuleList.get(0).getHtNo());
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


    @RequestMapping(value="/updateResolveTotal",method = RequestMethod.POST)
    public Result updateResolveTotal(@RequestBody  Map<String,Object> param) {
        Result result=new Result();
        result.setSuccess(false);
        try {

            if(param==null){
                result.setMessage("参数错误");
                return result;
            }

            if(param.get("htResolveList")==null){
                result.setMessage("分解数据为空，请查看数据是否正确");
                return result;
            }

            List<TbHtResolve> htResolveList = JsonUtil.parseJsonList(JsonUtil.toJsonStr(param.get("htResolveList")),TbHtResolve.class);


            boolean boo = contractProductService.updateResolveTotal(htResolveList);
            result.setSuccess(boo);
        }catch (Exception e) {
            logger.error("修改产值异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("修改产值失败");
        }
        return  result;
    }

    @RequestMapping(value="/queryHtSumPrice",method = RequestMethod.POST)
    public Result queryHtSumPrice(@RequestBody List<TbHtResolve> resolves){
        Result result=new Result();
        try{
            double suTotal=0.0;
            if(resolves!=null&&resolves.size()!=0){
                for(TbHtResolve tbHtResolve:resolves) {
                    suTotal = ArithmeticUtil.add(suTotal, tbHtResolve.getTotal());
                }
            }
            result.setSuccess(true);
            result.setObject(suTotal);
        }catch (Exception e){
            logger.error("数据相加异常"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据相加异常");
        }
        return result;
    }

}
