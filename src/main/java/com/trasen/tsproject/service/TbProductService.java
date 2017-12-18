package com.trasen.tsproject.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.dao.TbHtModuleMapper;
import com.trasen.tsproject.dao.TbProModuleMapper;
import com.trasen.tsproject.dao.TbProModulePriceMapper;
import com.trasen.tsproject.dao.TbProductMapper;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private Environment env;

    public List<TbProduct> getTbProductList(){
        List<TbProduct> tbProductList=tbProductMapper.selectProduct();
        tbProductList.forEach(tbProduct ->
            tbProduct.setProModuleList(tbProModuleMapper.selectProModule(tbProduct.getProCode()))
        );
        return tbProductList;
    }

    public List<TbProduct> queryTbProductList(){
        return tbProductMapper.selectProduct();
    }

    public List<TbProModule> selectProModule(String proCode){
        return tbProModuleMapper.selectProModule(proCode);
    }
    public List<TbProModule> queryProModuleList(List<String> ProCodeList){
        return tbProModuleMapper.queryProModuleList(ProCodeList);
    }

    public List<String> selectCleckModule(String htNo){
        return tbProModuleMapper.selectCleckModule(htNo);
    }

    @Transactional(rollbackFor=Exception.class)
    public void saveTbProductModule(String htNo, String price,String hospitalLevel, List<String> modList){
        tbHtModuleMapper.deleteHtModule(htNo);
        modList.stream().forEach(mod->saveTbHtModule(mod,htNo,hospitalLevel));
        contractProductService.getOutputValueOrSubtotal(htNo,Double.valueOf(price));
    }

    public void saveTbHtModule(String mod,String htNo,String hospitalLevel){
        TbHtModule htModule=new TbHtModule();
        TbProModulePrice tbProModulePrice=new TbProModulePrice();
        String[] modArray= mod.split(":");
        TbProModule tbProModule=tbProModuleMapper.selectProCode(Optional.ofNullable(modArray[0]).orElse("0"));
        Optional<TbProModule> tb=Optional.ofNullable(tbProModule);
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

    public List<String> getOldTbProModule(String htNo){
        return tbProModuleMapper.getOldTbProModule(htNo);
    }

    public int saveOrUpdateProductList(){
        String module_imis = env.getProperty("module_imis");
        if(module_imis==null){
            return 1;
        }
        String json= HttpUtil.connectURL(module_imis,"","POST");
        JSONObject dataJson = (JSONObject) JSONObject.parse(json);
        boolean boo=dataJson.getBoolean("success");
        if(boo){
            JSONArray dataJsonJSONArray= dataJson.getJSONArray("list");
            if(dataJsonJSONArray.size()>0){
                for (java.util.Iterator tor=dataJsonJSONArray.iterator();tor.hasNext();) {
                    JSONObject jsonObject = (JSONObject)tor.next();
                    Integer productId=jsonObject.getInteger("productId");
                    if(productId!=null){
                        TbProModule tbProduct=tbProModuleMapper.selectProductCount(String.valueOf(productId));
                        if(tbProduct==null){
                            TbProModule tbProductSave=new TbProModule();
                            tbProductSave.setModId(String.valueOf(productId));
                            tbProductSave.setModName(jsonObject.getString("productName"));
                            if(jsonObject.getString("type")!=null)tbProductSave.setProCode(jsonObject.getString("type"));
                            if(jsonObject.getString("productNo")!=null&&jsonObject.getString("productNo")!="") tbProductSave.setModNo(jsonObject.getString("productNo"));
                            if(jsonObject.getDate("createDate")!=null) {
                                tbProductSave.setCreated(jsonObject.getDate("createDate"));
                            }else{
                                tbProductSave.setCreated(new Date());
                            }
                            if(jsonObject.getInteger("versionCode")!=null){
                                tbProductSave.setVersion(jsonObject.getString("versionCode"));
                            } else{
                                tbProductSave.setVersion("1");
                            }
                            if(jsonObject.getInteger("latest")!=null){
                                tbProductSave.setIsVaild(jsonObject.getInteger("latest"));
                            }else{
                                tbProductSave.setIsVaild(0);
                            }
                            tbProModuleMapper.saveProduct(tbProductSave);
                        }
                    }
                }
            }
        }
        return 0;
    }

    public Map<String,Object> getAddModuleView(String htNo){
        Map<String,Object> param=new HashMap<>();
        List<TbProduct> tbProductList=queryTbProductList();
        List<TbHtModule> tbHtModuleList=tbProductMapper.selectAddModuleView(htNo);
        List<String> ProCodeList=tbHtModuleList.stream().map(tbHtModule -> tbHtModule.getProCode()).collect(Collectors.toList());
        List<String> newModuleList=tbHtModuleList.stream().map(tbHtModule -> tbHtModule.getModId()+":"+tbHtModule.getModName()).collect(Collectors.toList());
        List<String> proM=tbHtModuleList.stream().map(tbHtModule -> tbHtModule.getProCode()+"|"+tbHtModule.getModId()+":"+tbHtModule.getModName()).collect(Collectors.toList());
        //List<TbProModule> tbProModuleList=new ArrayList<>();
        //if(ProCodeList!=null&&ProCodeList.size()!=0) tbProModuleList=tbProModuleMapper.queryProModuleList(ProCodeList);
        param.put("proList",tbProductList);
        param.put("newProModuleList",ProCodeList);
        param.put("proM",proM);
        param.put("newModuleList",newModuleList);
        return param;
    }

    public PageInfo<TbProduct> queryProductModelList(int rows,int page,Map<String,String> param){
        PageHelper.startPage(page,rows);
        List<TbProduct> tbProductList=tbProductMapper.queryProductModelList(param);
        PageInfo<TbProduct> pagehelper = new PageInfo<TbProduct>(tbProductList);
        return pagehelper;
    }

    @Transactional(rollbackFor = Exception.class)
    public TbProduct saveTbProduct(TbProduct tbProduct){
        tbProductMapper.saveTbProduct(tbProduct);
        return tbProduct;
    }

    @Transactional(rollbackFor = Exception.class)
    public int  updateTbProduct(TbProduct tbProduct){
        return tbProductMapper.updateTbProduct(tbProduct);
    }

    public int deleteTbProduct(Integer pkid){
        return tbProductMapper.deleteTbProduct(pkid);
    }

    public PageInfo<TbProModule> queryTbProModuleList(int rows,int page,String proCode){
        PageHelper.startPage(page,rows);
        List<TbProModule> tbProModuleList=tbProModuleMapper.queryTbProModuleList(proCode);
        PageInfo<TbProModule> pagehelper = new PageInfo<TbProModule>(tbProModuleList);
        return pagehelper;
    }
    @Transactional(rollbackFor = Exception.class)
    public int updateProModel(TbProModule tbProModule){
        return tbProModuleMapper.updateProModel(tbProModule);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateIsVaild(String modId){
        return tbProModuleMapper.updateIsVaild(modId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProModel(TbProModule tbProModule){
        tbProModule.setModId(UUID.randomUUID().toString());
        tbProModule.setCreated(new Date());
        tbProModule.setIsVaild(1);
        tbProModuleMapper.saveProduct(tbProModule);
    }

    public PageInfo<TbProModulePrice> queryModelPriceList(int rows,int page,String modId){
        PageHelper.startPage(page,rows);
        List<TbProModulePrice> tbProModulePrices=tbProModulePriceMapper.queryModelPriceList(modId);
        PageInfo<TbProModulePrice> pagehelper = new PageInfo<TbProModulePrice>(tbProModulePrices);
        return pagehelper;
    }

    public int updateStandardPrice(TbProModulePrice tbProModulePrice){
        return tbProModulePriceMapper.updateStandardPrice(tbProModulePrice);
    }
    public int insertStandardPrice(TbProModulePrice tbProModulePrice){
        return tbProModulePriceMapper.insertStandardPrice(tbProModulePrice);
    }
    public int deleteStandardPrice(Integer pkid){
        return tbProModulePriceMapper.deleteStandardPrice(pkid);
    }
}
