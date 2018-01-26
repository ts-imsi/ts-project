package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TwfDict;
import com.trasen.tsproject.service.TwfDictService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/26
 */
@RestController
@RequestMapping(value="/twfDict")
public class TwfDictController{

    private static final Logger logger=Logger.getLogger(TwfDictController.class);

    @Autowired
    TwfDictService twfDictService;

    @PostMapping(value="/selectQuarters")
    public Map<String,Object> selectQuarters(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.get("page")==null||param.get("rows")==null||param.get("type")==null){
                result.put("message","参数错误");
                result.put("success",false);
                return result;
            }
            TwfDict twfDict=new TwfDict();
            twfDict.setType(Integer.valueOf(param.get("type")));
            if(param.get("selectName")!=null){
                twfDict.setName(param.get("selectName"));
            }
            PageInfo<TwfDict> twfDictPageInfo=twfDictService.selectTwfDictByType(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),twfDict);
            logger.info("数据查询条数"+twfDictPageInfo.getList().size());
            result.put("totalPages",twfDictPageInfo.getPages());
            result.put("pageNo",twfDictPageInfo.getPageNum());
            result.put("totalCount",twfDictPageInfo.getTotal());
            result.put("pageSize",twfDictPageInfo.getPageSize());
            result.put("list",twfDictPageInfo.getList());
            result.put("success",true);

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("message","数据查询失败");
            result.put("success",false);
        }
        return result;
    }

    @PostMapping(value="deleteTwfDict/{pkid}")
    public Result deleteTwfDict(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            twfDictService.deleteTwfDict(Optional.ofNullable(pkid).orElse(0));
            result.setSuccess(true);
            result.setMessage("数据删除成功");
        }catch (Exception e){
            logger.error("数据删除失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据删除失败");
        }
        return result;
    }

    @PostMapping(value="/saveTwfDict")
    public Result saveTwfDict(@RequestBody TwfDict twfDict){
        Result result=new Result();
        try{
            twfDictService.saveTwfDict(twfDict);
            result.setSuccess(true);
            result.setMessage("数据保存成功");
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }

    @PostMapping(value="/updateTwfDict")
    public Result updateTwfDict(@RequestBody TwfDict twfDict){
        Result result=new Result();
        try{
            twfDictService.updateTwfDict(twfDict);
            result.setSuccess(true);
            result.setMessage("数据更新成功");
        }catch (Exception e){
            logger.error("数据更新失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据更新失败");
        }
        return result;
    }
}
