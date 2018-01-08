package com.trasen.tsproject.dao;

import com.trasen.tsproject.model.TbWeixinCustomer;
import com.trasen.tsproject.util.MyMapper;

import java.util.List;

public interface TbWeixinCustomerMapper extends MyMapper<TbWeixinCustomer> {
    List<TbWeixinCustomer> selectWeixinCustomerList(TbWeixinCustomer tbWeixinCustomer);
    int deleteWxCus(Integer pkid);
    int updateWxCus(TbWeixinCustomer tbWeixinCustomer);
    int saveWxCus(TbWeixinCustomer tbWeixinCustomer);
    int updateWxCusCode(TbWeixinCustomer tbWeixinCustomer);
}