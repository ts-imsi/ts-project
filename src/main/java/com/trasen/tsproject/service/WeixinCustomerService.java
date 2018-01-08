package com.trasen.tsproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbWeixinCustomerMapper;
import com.trasen.tsproject.model.TbPlanTemplate;
import com.trasen.tsproject.model.TbWeixinCustomer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/5
 */
@Service
public class WeixinCustomerService {

    private static final  Logger logger= org.apache.log4j.Logger.getLogger(WeixinCustomerService.class);

    @Autowired
    TbWeixinCustomerMapper tbWeixinCustomerMapper;

    public PageInfo<TbWeixinCustomer> selectWeixinCustomerList(int page,int rows,TbWeixinCustomer tbWeixinCustomer){
        PageHelper.startPage(page,rows);
        List<TbWeixinCustomer> tbPlanTemplateList=tbWeixinCustomerMapper.selectWeixinCustomerList(tbWeixinCustomer);
        PageInfo<TbWeixinCustomer> pagehelper = new PageInfo<TbWeixinCustomer>(tbPlanTemplateList);
        return pagehelper;
    }

    public int deleteWxCus(Integer pkid){
        return tbWeixinCustomerMapper.deleteWxCus(pkid);
    }

    public int updateWxCus(TbWeixinCustomer tbWeixinCustomer){
        tbWeixinCustomer.setUpdated(new Date());
        tbWeixinCustomer.setOperator(VisitInfoHolder.getShowName());
        return tbWeixinCustomerMapper.updateWxCus(tbWeixinCustomer);
    }

    public int saveWxCus(TbWeixinCustomer tbWeixinCustomer){
        tbWeixinCustomer.setCreated(new Date());
        tbWeixinCustomer.setOperator(VisitInfoHolder.getShowName());
         int count=tbWeixinCustomerMapper.saveWxCus(tbWeixinCustomer);
         if(count>0){
             int uCon=updateWxCusCode(tbWeixinCustomer);
             return uCon;
         }
         return count;
    }

    public int updateWxCusCode(TbWeixinCustomer tbWeixinCustomer){
        tbWeixinCustomer.setInviteCode(tbWeixinCustomer.getPkid()+"-"+(int)((Math.random()*9+1)*1000));
        return tbWeixinCustomerMapper.updateWxCusCode(tbWeixinCustomer);
    }
}
