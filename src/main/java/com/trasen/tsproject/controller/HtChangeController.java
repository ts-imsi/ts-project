package com.trasen.tsproject.controller;

import cn.trasen.commons.util.StringUtil;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.model.TbHtChange;
import com.trasen.tsproject.model.TbHtHandover;
import com.trasen.tsproject.service.TbHtChangeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
}
