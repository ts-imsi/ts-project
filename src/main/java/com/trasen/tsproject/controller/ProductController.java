package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbProModule;
import com.trasen.tsproject.model.TbProduct;
import com.trasen.tsproject.service.TbProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/24
 */
@RestController
@RequestMapping(value="/product")
public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class);

    @Autowired
    private TbProductService tbProductService;

    @RequestMapping(value="/getTbProductList/{htNo}",method = RequestMethod.POST)
    public Map<String,Object> getTbProductList(@PathVariable String htNo){
        Map<String,Object> param=new HashMap<>();
        try{
            List<TbProduct> tbProductList=tbProductService.getTbProductList();
            List<String> tbProModuleList=tbProductService.selectCleckModule(htNo);
            param.put("object",tbProductList);
            param.put("checkList",tbProModuleList);
            param.put("success",true);
        }catch (Exception e){
            logger.error("数据查询错误:"+e.getMessage(),e);
            param.put("message","产品模块数据查询错误");
            param.put("success",false);
        }
        return param;
    }

    @RequestMapping(value="/getNewTbProductList",method = RequestMethod.POST)
    public Map<String,Object> getNewTbProductList(){
        Map<String,Object> param=new HashMap<>();
        try{
            List<TbProduct> tbProductList=tbProductService.getTbProductList();
            param.put("object",tbProductList);
            param.put("success",true);
        }catch (Exception e){
            logger.error("数据查询错误:"+e.getMessage(),e);
            param.put("message","产品模块数据查询错误");
            param.put("success",false);
        }
        return param;
    }

    @RequestMapping(value="/saveTbProductModule",method = RequestMethod.POST)
    public Result saveTbProductModule(@RequestBody Map<String,Object> param){
        Result result=new Result();
        try{
            if(param.get("htNo")==null||param.get("htNo").equals("")){
                result.setSuccess(false);
                result.setMessage("合同编号为空");
                return result;
            }
            if(param.get("htPrice")==null||param.get("htPrice").equals("")){
                result.setSuccess(false);
                result.setMessage("合同价格为空");
                return result;
            }
            if(param.get("hosLevel")==null||param.get("hosLevel").equals("")){
                param.put("hosLevel",0);
            }
            List<String> modList= (List<String>) param.get("moduleList");
            tbProductService.saveTbProductModule(param.get("htNo").toString(),param.get("htPrice").toString(),param.get("hosLevel").toString(),modList);
            result.setSuccess(true);
            result.setMessage("数据保存成功");
        }catch(Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }

    @RequestMapping(value="/getOldTbProModule/{htNo}",method = RequestMethod.POST)
    public Result getOldTbProModule(@PathVariable String htNo){
        Result result=new Result();
        try{
            Optional<String> op=Optional.ofNullable(htNo);
            if(!op.isPresent()){
                result.setMessage("合同编号参数传入错误");
                result.setSuccess(false);
            }else{
                List<String> oldModuleList=tbProductService.getOldTbProModule(op.get());
                result.setObject(oldModuleList);
                result.setSuccess(true);
            }
        }catch (Exception e){
            logger.error("查询老模板数据失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("查询老模板数据失败");
        }
        return result;
    }

    @RequestMapping(value = "/queryTbProductList",method = RequestMethod.POST)
    public Result queryTbProductList(){
        Result result=new Result();
        try{
            List<TbProduct> tbProductList=tbProductService.queryTbProductList();
            result.setObject(tbProductList);
            result.setSuccess(true);
        }catch(Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }
        return result;
    }
    @RequestMapping(value="/queryProModuleList",method = RequestMethod.POST)
    public Result queryProModuleList(@RequestBody List<String> proCodeList){
        Result result=new Result();
        try{
            List<TbProModule> tbProModuleList=tbProductService.queryProModuleList(Optional.ofNullable(proCodeList).orElse(null));
            result.setSuccess(true);
            result.setObject(tbProModuleList);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        return result;
    }

    @RequestMapping(value="/getAddModuleView/{htNo}",method = RequestMethod.POST)
    public Map<String,Object> getAddModuleView(@PathVariable String htNo){
        Map<String,Object> param=new HashMap<>();
        try{
            if(Optional.ofNullable(htNo).isPresent()){
                param=tbProductService.getAddModuleView(htNo);
                param.put("success",true);
            }else{
                param.put("success",false);
                param.put("message","参数传入错误");
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            param.put("success",false);
            param.put("message","数据查询失败");
        }
        return param;
    }
}
