package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.model.TbMsg;
import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.model.TbTemplateItem;
import com.trasen.tsproject.service.TbMsgService;
import com.trasen.tsproject.service.TbPersonnelService;
import com.trasen.tsproject.util.DateUtils;
import org.apache.commons.collections.MapUtils;
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
 * @date 2017/10/13
 */
@RestController
@RequestMapping(value="/tb_message")
public class MessageController {

    private static final Logger logger = Logger.getLogger(MessageController.class);

    @Autowired
    private TbMsgService tbMsgService;

    @Autowired
    private TbPersonnelService tbPersonnelService;

    @RequestMapping(value="/selectTbMsg",method = RequestMethod.POST)
    public Map<String,Object> selectTbMsg(@RequestBody Map<String,String> param){
        Map<String,Object> result=new HashMap<>();
        try{
            if(param.get("page")==null||param.get("rows")==null){
                result.put("messages","参数错误");
                result.put("success",false);
                return result;
            }
            PageInfo<TbMsg> tbMsgPageInfo=tbMsgService.selectTbMsg(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),param);
            logger.info("数据查询条数"+tbMsgPageInfo.getList().size());
            result.put("totalPages",tbMsgPageInfo.getPages());
            result.put("pageNo",tbMsgPageInfo.getPageNum());
            result.put("totalCount",tbMsgPageInfo.getTotal());
            result.put("pageSize",tbMsgPageInfo.getPageSize());
            result.put("list",tbMsgPageInfo.getList());
            result.put("success",true);
        }catch(Exception e){
            logger.error("信息查询失败=="+e.getMessage(),e);
            result.put("success",false);
            result.put("message","查询数据失败");
        }
        return result;
    }

    @RequestMapping(value="/getTbMsgById/{pkid}",method = RequestMethod.GET)
    public Result getTbMsgById(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            if(pkid==null){
                result.setSuccess(false);
                result.setMessage("参数错误");
                return result;
            }
            TbMsg tbMsg=tbMsgService.getTbMsgById(pkid);
            result.setSuccess(true);
            result.setObject(tbMsg);
        }catch (Exception e){
            logger.error("getTbMsgById查询失败=="+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("查询数据失败");
        }
        return result;
    }

    @RequestMapping(value="/getTodoMsg/{pkid}",method = RequestMethod.GET)
    public Result getTodoMsg(@PathVariable Integer pkid){
        Result result=new Result();
        try{
            if(pkid==null){
                result.setSuccess(false);
                result.setMessage("参数错误");
                return result;
            }
            TbMsg tbMsg=tbMsgService.getTodoMsg(pkid);
            result.setSuccess(true);
            result.setObject(tbMsg);
        }catch (Exception e){
            logger.error("getTodoMsg查询失败=="+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("查询数据失败");
        }
        return result;
    }

    @RequestMapping(value="/submitFlow",method = RequestMethod.POST)
    public Result submitFlow(@RequestBody TbMsg tbMsg){
        Result result=new Result();
        try{
            if(tbMsg==null){
                result.setMessage("参数错误");
                result.setSuccess(false);
            }else{
                if(tbMsg.getTaskKey()!=null&&"nk_check".equals(tbMsg.getTaskKey())){
                    //查分解表是否有生产部门确认
                    boolean boo = tbMsgService.checkAnalyze(tbMsg.getProcessId());
                    if(!boo){
                        result.setMessage("您未分解生产部门,请先分解!");
                        result.setSuccess(true);
                        return result;
                    }else{
                        //内控已分解
                        Map<String,Object> para = new HashMap<>();
                        para.put("operator", VisitInfoHolder.getShowName());
                        para.put("processId",tbMsg.getProcessId());
                        tbMsgService.updateRecount(para);
                    }
                }

                boolean boo=tbMsgService.submitFlow(tbMsg);

                if(boo){
                    if(tbMsg.getProcessKey().contains("handover")){
                        TbHtHandover tbHtHandover=tbMsgService.selectByProcessId(tbMsg.getProcessId());
                        if(Optional.ofNullable(tbHtHandover).isPresent()){
                            List<TbTemplateItem> tbTemplateItems=tbHtHandover.getContentJson();
                            tbTemplateItems.stream().forEach(tbTemplateItem -> addContentJson(tbTemplateItem,tbMsg));
                        }
                        tbMsgService.updateHandOverByProcessId(tbHtHandover);
                    }

                    result.setMessage("流程提交成功");
                    result.setSuccess(true);

                    if(tbMsg.getTaskKey()!=null&&"change_mg".equals(tbMsg.getTaskKey())){
                        //合同变更总经理审批时更新
                        Map<String,String> param=new HashMap<>();
                        param.put("status","1");
                        param.put("processId",tbMsg.getProcessId());
                        tbMsgService.updatehtChangeStatus(param);
                    }

                    if(tbMsg.getTaskKey()!=null&&"gm_check".equals(tbMsg.getTaskKey())){
                        Map<String,Object> para = new HashMap<>();
                        para.put("operator", VisitInfoHolder.getShowName());
                        para.put("processId",tbMsg.getProcessId());
                        para.put("status",2);//完成交接单流程
                        tbMsgService.updateStatus(para);
                    }


                }else{
                    result.setMessage("流程提交失败");
                    result.setSuccess(true);
                }
            }
        }catch (Exception e){
            logger.error("流程提交错误"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("流程提交错误");
        }
        return result;
    }
    @RequestMapping(value="/returnFlow",method = RequestMethod.POST)
    public Result returnFlow(@RequestBody TbMsg tbMsg){
        Result result=new Result();
        try{
            if(tbMsg==null){
                result.setMessage("参数错误");
                result.setSuccess(false);
            }else{
                boolean boo=tbMsgService.returnFlow(tbMsg);
                if(boo){
                    result.setMessage("流程驳回成功");
                    result.setSuccess(true);
                    if(tbMsg.getTaskKey()!=null&&"gm_check".equals(tbMsg.getTaskKey())){
                        //总经理驳回生产部门重新确认
                        logger.info("总经理交接单驳回,生产部门需重新确认===流程ID:"+tbMsg.getProcessId());
                        tbMsgService.pbReCheck(tbMsg.getProcessId());
                    }
                }else{
                    result.setMessage("流程驳回失败");
                    result.setSuccess(true);
                }
            }
        }catch (Exception e){
            logger.error("流程驳回错误"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("流程驳回错误");
        }
        return result;
    }

    @RequestMapping(value="/pdConfirm",method = RequestMethod.POST)
    public Result pdConfirm(@RequestBody TbMsg tbMsg){
        Result result=new Result();
        try{
            if(tbMsg==null){
                result.setMessage("参数错误");
                result.setSuccess(false);
            }else{
                boolean boo=tbMsgService.pdConfirm(tbMsg);
                if(boo){
                    if(tbMsg.getProcessKey().contains("handover")){
                        TbHtHandover tbHtHandover=tbMsgService.selectByProcessId(tbMsg.getProcessId());
                        if(Optional.ofNullable(tbHtHandover).isPresent()){
                            List<TbTemplateItem> tbTemplateItems=tbHtHandover.getContentJson();
                            addPdSign(tbTemplateItems,tbMsg);
                        }
                        tbMsgService.updateHandOverByProcessId(tbHtHandover);
                    }
                    result.setMessage("确认成功");
                    result.setSuccess(true);
                }else{
                    result.setMessage("确认失败");
                    result.setSuccess(true);
                }
            }
        }catch (Exception e){
            logger.error("生产确认错误"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("生产确认错误");
        }
        return result;
    }

    public void addContentJson(TbTemplateItem tbTemplateItem,TbMsg tbMsg){
        if(tbMsg.getTaskKey().equals("sale_commit")){
            if(tbTemplateItem.getCode().equals("saleSign")){
                tbTemplateItem.setModule(VisitInfoHolder.getShowName());
                tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
            }
        }else if(tbMsg.getTaskKey().equals("nk_check")){
            if(tbTemplateItem.getCode().equals("nkSign")){
                tbTemplateItem.setModule(VisitInfoHolder.getShowName());
                tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
            }
        }else if(tbMsg.getTaskKey().equals("em_check")){
            if(tbTemplateItem.getCode().equals("emSign")){
                tbTemplateItem.setModule(VisitInfoHolder.getShowName());
                tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
            }
        }else if(tbMsg.getTaskKey().equals("pm_check")){
            if(tbTemplateItem.getCode().equals("proSign")){
                tbTemplateItem.setModule(VisitInfoHolder.getShowName());
                tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
            }
        }else if(tbMsg.getTaskKey().equals("gm_check")){
            if(tbTemplateItem.getCode().equals("zjlSign")){
                tbTemplateItem.setModule(VisitInfoHolder.getShowName());
                tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
            }
        }
    }

    public void addPdSign(List<TbTemplateItem> tbTemplateItems,TbMsg tbMsg){
        if(tbMsg.getTaskKey().equals("pd_check")){
            TbPersonnel tbPersonnel=tbPersonnelService.selectTbPersonnel();
            //生产部门特殊处理,自动签名
            TbTemplateItem tbTemplateItem = new TbTemplateItem();
            tbTemplateItem.setLevel(0);
            tbTemplateItem.setModule(VisitInfoHolder.getShowName());
            tbTemplateItem.setValue(DateUtils.getDate("yyyy-MM-dd"));
            tbTemplateItem.setName(tbPersonnel.getDepName()+"签字");
            tbTemplateItem.setPx(22);
            tbTemplateItem.setCode("pdSign");
            tbTemplateItems.add(tbTemplateItem);
        }
    }



    @RequestMapping(value="/getMsgCount",method = RequestMethod.GET)
    public Result getMsgCount(){
        Result result=new Result();
        try{
            Map<String,Object> map = tbMsgService.indexMsgCount();
            result.setSuccess(true);
            result.setObject(map);
        }catch (Exception e){
            logger.error("getMsgCount查询失败=="+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("查询数据失败");
        }
        return result;
    }
}
