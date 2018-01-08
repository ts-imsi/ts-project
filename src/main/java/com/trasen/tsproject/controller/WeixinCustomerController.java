package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbWeixinCustomer;
import com.trasen.tsproject.service.WeixinCustomerService;
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
 * @date 2018/1/5
 */
@RestController
@RequestMapping("/weixinCus")
public class WeixinCustomerController {

    private static final Logger logger=Logger.getLogger(WeixinCustomerController.class);

    @Autowired
    WeixinCustomerService weixinCustomerService;

    @RequestMapping(value="/selectWeixinCustomerList",method = RequestMethod.POST)
    public Map<String,Object> selectWeixinCustomerList(@RequestBody  Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        try {
            if(param.get("page")==null||param.get("rows")==null){
                paramMap.put("message","参数错误");
                paramMap.put("success",false);
                return paramMap;
            }
            TbWeixinCustomer tbWeixinCustomer=new TbWeixinCustomer();
            if(!StringUtil.isEmpty(param.get("selectName"))&&!param.get("selectName").equals("")){
                tbWeixinCustomer.setName(param.get("selectName"));
                tbWeixinCustomer.setLinkMan(param.get("selectName"));
            }

            PageInfo<TbWeixinCustomer> customerPageInfo=weixinCustomerService.selectWeixinCustomerList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbWeixinCustomer);
            logger.info("数据查询条数"+customerPageInfo.getList().size());
            paramMap.put("totalPages",customerPageInfo.getPages());
            paramMap.put("pageNo",customerPageInfo.getPageNum());
            paramMap.put("totalCount",customerPageInfo.getTotal());
            paramMap.put("pageSize",customerPageInfo.getPageSize());
            paramMap.put("list",customerPageInfo.getList());
            paramMap.put("success",true);
            return paramMap;
        }catch (Exception e) {
            logger.error("数据查询失败"+e.getMessage(),e);
            paramMap.put("message","查询数据失败");
            paramMap.put("success",false);
            return paramMap;
        }
    }

    @RequestMapping(value="/deleteWxCus/{pkid}",method = RequestMethod.POST)
    public Result deleteWxCus(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            weixinCustomerService.deleteWxCus(Optional.ofNullable(pkid).orElse(0));
            result.setSuccess(true);
            result.setMessage("数据删除成功");
        }catch (Exception e){
            logger.error("数据删除失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据删除失败");
        }
        return result;
    }

    @RequestMapping(value="/updateWxCus",method = RequestMethod.POST)
    public Result updateWxCus(@RequestBody TbWeixinCustomer tbWeixinCustomer){
        Result result=new Result();
        try{
            if(tbWeixinCustomer==null||tbWeixinCustomer.getPkid()==null){
                result.setSuccess(false);
                result.setMessage("数据更新参数失败");
            }else{
                weixinCustomerService.updateWxCus(tbWeixinCustomer);
                result.setMessage("数据更新成功");
                result.setSuccess(true);
            }
        }catch (Exception e){
            logger.error("数据更新失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据更新失败");
        }
        return result;
    }

    @RequestMapping(value="/saveWxCus",method = RequestMethod.POST)
    public Result saveWxCus(@RequestBody TbWeixinCustomer tbWeixinCustomer){
        Result result=new Result();
        try{
            if(tbWeixinCustomer==null){
                result.setSuccess(false);
                result.setMessage("数据保存参数失败");
            }else{
                weixinCustomerService.saveWxCus(tbWeixinCustomer);
                result.setMessage("数据保存成功");
                result.setSuccess(true);
            }
        }catch (Exception e){
            logger.error("数据保存失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据保存失败");
        }
        return result;
    }
}
