package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.service.ContractProductService;
import com.trasen.tsproject.service.TbHtChangeService;
import com.trasen.tsproject.service.TbPersonnelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/12
 */
@RestController
@RequestMapping(value="/htChange")
public class HtChangeController {

    private static final Logger logger = Logger.getLogger(HandoverController.class);

    @Autowired
    private TbHtChangeService tbHtChangeService;

    @Autowired
    private ContractProductService contractProductService;


    @Autowired
    private TbPersonnelService tbPersonnelService;

    @RequestMapping(value="/getHtChangeList",method = RequestMethod.POST)
    public Map<String,Object> getHtChangeList(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<String,Object>();
        try {
            if(param.get("page")==null||param.get("rows")==null){
                paramMap.put("messages","参数错误");
                paramMap.put("success",false);
                return paramMap;
            }
            TbHtChange tbHtChange=new TbHtChange();
            if(!StringUtil.isEmpty(param.get("selectName"))){
                tbHtChange.setHtNo(param.get("selectName"));
                tbHtChange.setHtName(param.get("selectName"));
                tbHtChange.setCustomerName(param.get("selectName"));
            }
            if(!StringUtil.isEmpty(param.get("selectType"))){
                tbHtChange.setType(param.get("selectType"));
            }
            if(!StringUtil.isEmpty(param.get("status"))){
                tbHtChange.setStatus(Integer.valueOf(param.get("status")));
            }
            PageInfo<TbHtChange> tbHtChangePageInfo=tbHtChangeService.getHtChangeList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbHtChange);
            logger.info("数据查询条数"+tbHtChangePageInfo.getList().size());
            paramMap.put("totalPages",tbHtChangePageInfo.getPages());
            paramMap.put("pageNo",tbHtChangePageInfo.getPageNum());
            paramMap.put("totalCount",tbHtChangePageInfo.getTotal());
            paramMap.put("pageSize",tbHtChangePageInfo.getPageSize());
            paramMap.put("list",tbHtChangePageInfo.getList());
            paramMap.put("success",true);
            return paramMap;
        }catch (Exception e) {
            paramMap.put("messages","查询合同变更错误");
            paramMap.put("success",false);
            return paramMap;
        }
    }

    @RequestMapping(value="/getOaContractListByOwner",method = RequestMethod.POST)
    public Result getOaContractListByOwner(){
        Result result=new Result();
        try {
            //TODO 查询当前人员下面的合同
            Map<String, String> param = new HashMap<>();
            param.put("contractOwner", VisitInfoHolder.getShowName());
            List<ContractInfo> contractInfoList = contractProductService.getOaContractListByOwner(param);
            result.setSuccess(true);
            result.setObject(contractInfoList);
        }catch(Exception e){
            logger.error("获取当前人员下面的合同数据异常" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取当前人员下面的合同数据异常");
        }
        return result;
    }

    @RequestMapping(value="/selectTbPersonnel",method = RequestMethod.POST)
    public Result selectTbPersonnel(){
        Result result=new Result();
        try {
            TbPersonnel tbPersonnel=tbPersonnelService.selectTbPersonnel();
            result.setSuccess(true);
            result.setObject(tbPersonnel);
        }catch (Exception e){
            logger.error("获取当前人员信息失败" + e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("获取当前人员信息失败");
        }
        return result;
    }

    @RequestMapping(value="/applySubmit",method = RequestMethod.POST)
    public Result applySubmit(@RequestBody Map<String,Object> param){
        Result result=new Result();

        try {
            if(param.isEmpty()){
                result.setSuccess(false);
                result.setMessage("参数传递失败");
                return result;
            }
            Map<String,Object> maptb=(Map<String,Object>)param.get("htchange");;
            TbHtChange tbHtChange=new TbHtChange();
            tbHtChange.setHtNo(Optional.ofNullable(maptb.get("htNo")).orElse("0").toString());
            tbHtChange.setHtName(Optional.ofNullable(maptb.get("htName")).orElse("0").toString());
            tbHtChange.setType(Optional.ofNullable(maptb.get("type")).orElse("0").toString());
            tbHtChange.setHtOwner(Optional.ofNullable(maptb.get("htOwner")).orElse("0").toString());
            tbHtChange.setStatus(Integer.valueOf(Optional.ofNullable(maptb.get("status")).orElse("0").toString()));
            tbHtChange.setApplicationDept(Optional.ofNullable(maptb.get("applicationDept")).orElse("0").toString());
            tbHtChange.setChangeContent(Optional.ofNullable(maptb.get("changeContent")).orElse("0").toString());
            tbHtChange.setWorkNum(Optional.ofNullable(maptb.get("workNum")).orElse("0").toString());
            tbHtChange.setCreated(new Date());
            tbHtChange.setCustomerName(Optional.ofNullable(maptb.get("customerName")).orElse("0").toString());
            tbHtChange.setCreateUser(Optional.ofNullable(maptb.get("createUser")).orElse("0").toString());
            tbHtChange.setSignDate(Optional.ofNullable(maptb.get("signDate")).orElse("0").toString());
            tbHtChange.setRemark(Optional.ofNullable(maptb.get("remark")).orElse("备注").toString());
            if ( tbHtChange== null) {
                result.setSuccess(false);
                result.setMessage("合同变更参数传递失败");
                return result;
            }
           List<String> oldModuleList=(List<String>) param.get("oldModuleList");
            List<String> newModuleList=(List<String>) param.get("newModuleList");
            if(param.get("hosLevel")==null){
                param.put("hosLevel",0);
            }
            Optional<Object> opt=Optional.ofNullable(param.get("price"));
            tbHtChangeService.applySubmit(tbHtChange,oldModuleList,newModuleList,param.get("hosLevel").toString(),opt.orElse("1.0").toString());
            result.setSuccess(true);
            result.setMessage("流程启动成功");
        }catch (Exception e){
            logger.error("流程启动失败=="+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("流程启动失败");
        }
        return result;
    }

    @RequestMapping(value="/getContractByHtNo/{contractNo}",method = RequestMethod.POST)
    public Map<String,Object> getContractByHtNo(@PathVariable String contractNo){
        Map<String,Object> result=new HashMap<>();
        try{
            Optional<String> optional=Optional.ofNullable(contractNo);
            if(!optional.isPresent()){
                result.put("success",false);
                result.put("message","参数为空");
            }else{
             Map<String,Object>  param=tbHtChangeService.getContractByHtNo(optional.get());
             return param;
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;

    }

    @RequestMapping(value="/getHtChangeView/{htNo}",method = RequestMethod.POST)
    public Map<String,Object> getHtChangeView(@PathVariable String htNo){
        Map<String,Object> result=new HashMap<>();
        try{
            Map<String,Object> param=tbHtChangeService.getHtChangeView(Optional.ofNullable(htNo).orElse("0"));
            result.put("newModule",param.get("newModule"));
            result.put("oldModule",param.get("oldModule"));
            result.put("success",true);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("success",false);
            result.put("message","数据查询失败");
        }
        return result;
    }
}
