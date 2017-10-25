package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtModuleMapper;
import com.trasen.tsproject.dao.TbProModuleMapper;
import com.trasen.tsproject.dao.TbProModulePriceMapper;
import com.trasen.tsproject.dao.TbProductMapper;
import com.trasen.tsproject.model.TbHtModule;
import com.trasen.tsproject.model.TbProModule;
import com.trasen.tsproject.model.TbProModulePrice;
import com.trasen.tsproject.model.TbProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/10/24
 */
@Service
public class TbProductService {

    @Autowired
    private TbProductMapper tbProductMapper;

    @Autowired
    private TbProModuleMapper tbProModuleMapper;

    @Autowired
    private TbHtModuleMapper tbHtModuleMapper;

    @Autowired
    private TbProModulePriceMapper tbProModulePriceMapper;

    @Autowired
    private ContractProductService contractProductService;

    public List<TbProduct> getTbProductList(){
        List<TbProduct> tbProductList=tbProductMapper.selectProduct();
        tbProductList.forEach(tbProduct ->
            tbProduct.setProModuleList(tbProModuleMapper.selectProModule(tbProduct.getProCode()))
        );
        return tbProductList;
    }

    public List<String> selectCleckModule(String htNo){
        return tbProModuleMapper.selectCleckModule(htNo);
    }

    @Transactional(rollbackFor=Exception.class)
    public void saveTbProductModule(String htNo, String price,String hospitalLevel, List<String> modIdList){
        tbHtModuleMapper.deleteHtModule(htNo);
        modIdList.stream().forEach(modId->saveTbHtModule(modId,htNo,hospitalLevel));
        contractProductService.getOutputValueOrSubtotal(htNo,Double.valueOf(price));
    }

    public void saveTbHtModule(String modId,String htNo,String hospitalLevel){
        TbHtModule htModule=new TbHtModule();
        TbProModulePrice tbProModulePrice=new TbProModulePrice();
        TbProModule tbProModule=tbProModuleMapper.selectProCode(modId);
        Optional<TbProModule> tb=Optional.of(tbProModule);
        htModule.setHtNo(htNo);
        htModule.setProCode(tb.orElse(null).getProCode());
        htModule.setModId(tb.orElse(null).getModId());
        htModule.setOperator(VisitInfoHolder.getUserId());
        htModule.setUpdated(new Date());

        tbProModulePrice.setModId(tbProModule.getModId());
        tbProModulePrice.setHospitalLevel(hospitalLevel);
        tbProModulePrice=tbProModulePriceMapper.selectStandardPrice(tbProModulePrice);
        //设置标准价
        if(tbProModulePrice==null){
            htModule.setPrice(1.0);
        }else{
            htModule.setPrice(tbProModulePrice.getStandardPrice());
        }
        tbHtModuleMapper.saveHtModule(htModule);
    }
}
