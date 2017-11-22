package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.dao.TbPlanItemMapper;
import com.trasen.tsproject.model.ExceptionPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/20
 */
@Service
public class ExceptionPlanService {

    @Autowired
    TbPlanItemMapper tbPlanItemMapper;

    public PageInfo<ExceptionPlan> selectExceptionPlan(Map<String,Object> param){
        List<String> checkTagList=(List<String>)param.get("checkTag");
        List<String> noCheckTagList=(List<String>)param.get("noCheckTag");

        /*StringBuffer checkBuff=new StringBuffer();
        checkTagList.stream().forEach(s->checkBuff.append("'"+s+"'"+","));
        param.put("checkTag", StringUtils.substringBeforeLast(checkBuff.toString(),","));
        StringBuffer noCheckBuff=new StringBuffer();
        noCheckTagList.stream().forEach(s->noCheckBuff.append("'"+s+"'"+","));
        param.put("noCheckTag", StringUtils.substringBeforeLast(noCheckBuff.toString(),","));*/
        if(checkTagList!=null&&checkTagList.size()!=0){
            param.put("checkTagCount",checkTagList.size());
        }
        if(noCheckTagList!=null&&noCheckTagList.size()!=0){
            param.put("noCheckTagCount",noCheckTagList.size());
        }
        PageHelper.startPage(Integer.valueOf(param.get("page").toString()),Integer.valueOf(param.get("rows").toString()));
        List<ExceptionPlan> exceptionPlans=tbPlanItemMapper.selectExceptionPlan(param);
        PageInfo<ExceptionPlan> pagehelper = new PageInfo<ExceptionPlan>(exceptionPlans);
        return pagehelper;
    }
}
