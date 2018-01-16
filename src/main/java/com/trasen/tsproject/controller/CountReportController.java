package com.trasen.tsproject.controller;

import cn.trasen.commons.util.DownloadExcelUtil;
import cn.trasen.commons.util.StringUtil;
import cn.trasen.core.entity.Result;
import com.github.pagehelper.PageInfo;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.ContractInfo;
import com.trasen.tsproject.model.ExceptionPlan;
import com.trasen.tsproject.model.TbOutputValue;
import com.trasen.tsproject.model.TbOutputValueCount;
import com.trasen.tsproject.service.CountReportService;
import jxl.CellType;
import jxl.write.DateFormat;
import jxl.write.WriteException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhangxiahui on 18/1/9.
 */
@RestController
@RequestMapping(value="/countReport")
public class CountReportController {

    private static final Logger logger = Logger.getLogger(CountReportController.class);

    @Autowired
    CountReportService countReportService;

    @RequestMapping(value="/getcountReportList",method = RequestMethod.POST)
    public Map<String,Object> getcountReportList(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<>();
        try{
            if(param.isEmpty()){
                paramMap.put("success",false);
                paramMap.put("message","参数参入错误");
                return paramMap;
            }
            if(!Optional.ofNullable(param.get("page")).isPresent()||!Optional.ofNullable(param.get("rows")).isPresent()){
                paramMap.put("message","分页参数传入失败");
                paramMap.put("success",false);
                return paramMap;
            }
            TbOutputValueCount tbOutputValueCount=new TbOutputValueCount();
            if(Optional.ofNullable(param.get("year")).isPresent()&&!Optional.ofNullable(param.get("year")).get().equals("")){
                tbOutputValueCount.setYear(param.get("year"));
            }

            PageInfo<TbOutputValueCount> valueCountPageInfo=countReportService.getcountReportList(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),tbOutputValueCount);
            logger.info("数据查询条数"+valueCountPageInfo.getList().size());
            paramMap.put("totalPages",valueCountPageInfo.getPages());
            paramMap.put("pageNo",valueCountPageInfo.getPageNum());
            paramMap.put("totalCount",valueCountPageInfo.getTotal());
            paramMap.put("pageSize",valueCountPageInfo.getPageSize());
            paramMap.put("list",valueCountPageInfo.getList());
            paramMap.put("success",true);

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            paramMap.put("success",false);
            paramMap.put("message","数据查询失败");
        }
        return paramMap;
    }

    @RequestMapping(value="/getCountReport",method = RequestMethod.POST)
    public Result getCountReport(@RequestBody  Map<String,String> param){
        Result result=new Result();
        try{
            if(param.isEmpty()){
                result.setMessage("数据参数失败");
                result.setSuccess(false);
            }else{
                if(!param.get("selectType").equals("")&&param.get("selectType")!=null){
                    List<TbOutputValueCount> tbOutputValueCounts=new ArrayList<>();
                    Map<String,Object> pMap=new HashMap<>();
                    if(param.get("selectType").equals("dept")){
                        tbOutputValueCounts=countReportService.getCountRByDept(param.get("year"));
                    }else if(param.get("selectType").equals("pro")){
                        tbOutputValueCounts=countReportService.getCountRByPro(param.get("year"));
                    }else if(param.get("selectType").equals("proLine")){
                        tbOutputValueCounts=countReportService.getCountRByProline(param.get("year"));
                    }else{
                        result.setMessage("数据参数错误");
                        result.setSuccess(false);
                        return result;
                    }
                    pMap.put("tbOutputValueCounts",tbOutputValueCounts);
                    double unfinishedCount=0.0;
                    double finishedCount=0.0;
                    double totalCount=0.0;
                    double nextUnCount=0.0;
                    for(TbOutputValueCount tbOutputValueCount:tbOutputValueCounts){
                        unfinishedCount=add(tbOutputValueCount.getUnfinished(),unfinishedCount);
                        finishedCount=add(tbOutputValueCount.getFinished(),finishedCount);
                        totalCount=add(tbOutputValueCount.getTotal(),totalCount);
                        nextUnCount=add(tbOutputValueCount.getLastUnFinished(),nextUnCount);
                    }
                    pMap.put("unfinishedCount",unfinishedCount);
                    pMap.put("finishedCount",finishedCount);
                    pMap.put("totalCount",totalCount);
                    pMap.put("nextUnCount",nextUnCount);
                    result.setObject(pMap);
                    result.setSuccess(true);
                }else{
                    result.setSuccess(false);
                    result.setMessage("数据参数失败");
                }
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setMessage("数据查询失败");
            result.setSuccess(false);
        }
        return result;
    }

    @RequestMapping(value="/countOutputValue",method = RequestMethod.GET)
    public Result countOutputValue(){
        //结果集
        Result result = new Result();
        result.setStatusCode(0);
        result.setSuccess(false);
        try {
            countReportService.countOutputValue();
            result.setSuccess(true);
            result.setStatusCode(1);
        } catch (IllegalArgumentException e) {
            logger.error("产值统计异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("产值统计异常" + e.getMessage(), e);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @RequestMapping(value="/getOutputDetailed",method = RequestMethod.POST)
    public Map<String,Object> getOutputDetailed(@RequestBody Map<String,String> param){
        Map<String,Object> paramMap=new HashMap<>();
        try{
            if(param.isEmpty()){
                paramMap.put("success",false);
                paramMap.put("message","参数参入错误");
                return paramMap;
            }
            if(!Optional.ofNullable(param.get("page")).isPresent()||!Optional.ofNullable(param.get("rows")).isPresent()){
                paramMap.put("message","分页参数传入失败");
                paramMap.put("success",false);
                return paramMap;
            }
            Optional<String> opYear=Optional.ofNullable(param.get("year"));
            Optional<String> opType=Optional.ofNullable(param.get("selectType"));
            Optional<String> opName=Optional.ofNullable(param.get("selectName"));

            if(!opYear.isPresent()&&opYear.get().equals("")&&!opType.isPresent()&&opType.get().equals("")&&!opName.isPresent()&&opName.get().equals("")){
                paramMap.put("message","年份和类型传入失败");
                paramMap.put("success",false);
                return paramMap;
            }
            PageInfo<TbOutputValue> valuePageInfo=null;
            if(opType.get().equals("dept")){
                 valuePageInfo=countReportService.getOutPutByDept(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),opYear.get(),opName.get());
            }else if(opType.get().equals("pro")){
                 valuePageInfo=countReportService.getOutPutByPro(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),opYear.get(),opName.get());
            }else{
                 valuePageInfo=countReportService.getOutPutByProLine(Integer.valueOf(param.get("page")),Integer.valueOf(param.get("rows")),opYear.get(),opName.get());
            }
            logger.info("数据查询条数"+valuePageInfo.getList().size());
            paramMap.put("totalPages",valuePageInfo.getPages());
            paramMap.put("pageNo",valuePageInfo.getPageNum());
            paramMap.put("totalCount",valuePageInfo.getTotal());
            paramMap.put("pageSize",valuePageInfo.getPageSize());
            paramMap.put("list",valuePageInfo.getList());
            paramMap.put("success",true);

        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            paramMap.put("success",false);
            paramMap.put("message","数据查询失败");
        }
        return paramMap;
    }

    @RequestMapping(value = "/excelCountReportExport", method = RequestMethod.GET)
    public void excelCountReportExport(HttpServletResponse response, HttpServletRequest request) throws IOException, WriteException {
        String year = request.getParameter("year");
        List<TbOutputValueCount> tbOutputValueCounts=countReportService.excelCountReportExport(year);
        DownloadExcelUtil downloadExcelUtil= null;
        try{
            String fileName=year+"产值统计"+".xls";
            downloadExcelUtil=new DownloadExcelUtil(response,fileName,"产值统计");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            downloadExcelUtil.addCell(0,0,"合同号", CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(1,0,"客户",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(2,0,"合同名称",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(3,0,"合同额",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(4,0,"已完成",CellType.LABEL,dateFormat,false,false);
            downloadExcelUtil.addCell(5,0,"未完成",CellType.LABEL,dateFormat,false,false);

            for(int i=0;i<tbOutputValueCounts.size();i++){
                if(tbOutputValueCounts.get(i).getHtNo()==null){
                    downloadExcelUtil.addCell(0,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(0,i+1,tbOutputValueCounts.get(i).getHtNo(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbOutputValueCounts.get(i).getCustomerName()==null){
                    downloadExcelUtil.addCell(1,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(1,i+1,tbOutputValueCounts.get(i).getCustomerName(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbOutputValueCounts.get(i).getHtName()==null){
                    downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(2,i+1,tbOutputValueCounts.get(i).getHtName(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbOutputValueCounts.get(i).getTotal()==null){
                    downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(3,i+1,tbOutputValueCounts.get(i).getTotal(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbOutputValueCounts.get(i).getFinished()==null){
                    downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(4,i+1,tbOutputValueCounts.get(i).getFinished(),CellType.LABEL,dateFormat,false,false);
                }
                if(tbOutputValueCounts.get(i).getUnfinished()==null){
                    downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(5,i+1,tbOutputValueCounts.get(i).getUnfinished(),CellType.LABEL,dateFormat,false,false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/excelCountReportByTypeExport", method = RequestMethod.GET)
    public void excelCountReportByTypeExport(HttpServletResponse response, HttpServletRequest request) throws IOException, WriteException {
        String year = request.getParameter("year");
        String selectType=request.getParameter("selectType");
        DownloadExcelUtil downloadExcelUtil= null;
        try{
            String fileName=year+"产值统计"+".xls";
            downloadExcelUtil=new DownloadExcelUtil(response,fileName,"产值统计");
            DateFormat dateFormat=new DateFormat("yyyy-mm-dd");
            downloadExcelUtil.addCell(0,0,"名称", CellType.LABEL,dateFormat,false,false);
            if(selectType.equals("pro")) {
                downloadExcelUtil.addCell(1,0,"上年结转合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(2,0,"上年结转合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(3,0,"今年新签合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(4,0,"已完成合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(5,0,"待完成合同额",CellType.LABEL,dateFormat,false,false);
            }else{
                downloadExcelUtil.addCell(1,0,"上年结转合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(2,0,"今年新签合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(3,0,"已完成合同额",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(4,0,"待完成合同额",CellType.LABEL,dateFormat,false,false);
            }



            List<TbOutputValueCount> tbOutputValueCounts=new ArrayList<>();
            if(selectType.equals("dept")){
                tbOutputValueCounts=countReportService.getCountRByDept(year);
            }else if(selectType.equals("pro")){
                tbOutputValueCounts=countReportService.getCountRByPro(year);
            }else if(selectType.equals("proLine")){
                tbOutputValueCounts=countReportService.getCountRByProline(year);
            }else{
                tbOutputValueCounts=countReportService.getCountRByDept(year);
            }

            double unfinishedCount=0.0;
            double finishedCount=0.0;
            double totalCount=0.0;
            double nextUnCount=0.0;

            for(int i=0;i<tbOutputValueCounts.size();i++){
                if(tbOutputValueCounts.get(i).getName()==null){
                    downloadExcelUtil.addCell(0,i+1,"",CellType.LABEL,dateFormat,false,false);
                }else{
                    downloadExcelUtil.addCell(0,i+1,tbOutputValueCounts.get(i).getName(),CellType.LABEL,dateFormat,false,false);
                }
                if(selectType.equals("pro")) {
                    if(tbOutputValueCounts.get(i).getProLine()==null){
                        downloadExcelUtil.addCell(1,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(1,i+1,tbOutputValueCounts.get(i).getProLine(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getLastUnFinished()==null){
                        downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        //上年未结转合同额
                        nextUnCount=add(nextUnCount,tbOutputValueCounts.get(i).getLastUnFinished());
                        downloadExcelUtil.addCell(2,i+1,tbOutputValueCounts.get(i).getLastUnFinished(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getTotal()==null){
                        downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        //今年新增合同额
                        totalCount=add(totalCount,tbOutputValueCounts.get(i).getTotal());
                        downloadExcelUtil.addCell(3,i+1,tbOutputValueCounts.get(i).getTotal(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getFinished()==null){
                        downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        //已完成合同额
                        finishedCount=add(finishedCount,tbOutputValueCounts.get(i).getFinished());

                        downloadExcelUtil.addCell(4,i+1,tbOutputValueCounts.get(i).getFinished(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getUnfinished()==null){
                        downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        //今年未完成
                        unfinishedCount=add(unfinishedCount,tbOutputValueCounts.get(i).getUnfinished());

                        downloadExcelUtil.addCell(5,i+1,tbOutputValueCounts.get(i).getUnfinished(),CellType.LABEL,dateFormat,false,false);
                    }
                }else{
                    if(tbOutputValueCounts.get(i).getLastUnFinished()==null){
                        downloadExcelUtil.addCell(1,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        //上年未结转合同额
                        nextUnCount=add(nextUnCount,tbOutputValueCounts.get(i).getLastUnFinished());
                        downloadExcelUtil.addCell(1,i+1,tbOutputValueCounts.get(i).getLastUnFinished(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getTotal()==null){
                        downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{

                        //今年新增合同额
                        totalCount=add(totalCount,tbOutputValueCounts.get(i).getTotal());
                        downloadExcelUtil.addCell(2,i+1,tbOutputValueCounts.get(i).getTotal(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getFinished()==null){
                        downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{

                        //已完成合同额
                        finishedCount=add(finishedCount,tbOutputValueCounts.get(i).getFinished());

                        downloadExcelUtil.addCell(3,i+1,tbOutputValueCounts.get(i).getFinished(),CellType.LABEL,dateFormat,false,false);
                    }
                    if(tbOutputValueCounts.get(i).getUnfinished()==null){
                        downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{

                        //今年未完成
                        unfinishedCount=add(unfinishedCount,tbOutputValueCounts.get(i).getUnfinished());

                        downloadExcelUtil.addCell(4,i+1,tbOutputValueCounts.get(i).getUnfinished(),CellType.LABEL,dateFormat,false,false);
                    }
                }
            }

            int size=tbOutputValueCounts.size();
            downloadExcelUtil.addCell(0,size+1,"总计",CellType.LABEL,dateFormat,false,false);
            if(selectType.equals("pro")) {
                downloadExcelUtil.addCell(1,size+1,"",CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(2,size+1,nextUnCount,CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(3,size+1,totalCount,CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(4,size+1,finishedCount,CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(5,size+1,unfinishedCount,CellType.LABEL,dateFormat,false,false);
            }else{
                downloadExcelUtil.addCell(1,size+1,nextUnCount,CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(2,size+1,totalCount,CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(3,size+1,finishedCount,CellType.LABEL,dateFormat,false,false);
                downloadExcelUtil.addCell(4,size+1,unfinishedCount,CellType.LABEL,dateFormat,false,false);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                downloadExcelUtil.close();
            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @RequestMapping(value = "/getOutputDetailedExport", method = RequestMethod.GET)
    public void getOutputDetailedExport (HttpServletResponse response, HttpServletRequest request) throws IOException, WriteException {
            String year = request.getParameter("year");
            String selectType=request.getParameter("selectType");
            String selectName=request.getParameter("selectName");
            Optional<String> opYear=Optional.ofNullable(year);
            Optional<String> opType=Optional.ofNullable(selectType);
            Optional<String> opName=Optional.ofNullable(selectName);
            List<TbOutputValue> tbOutputValues=new ArrayList<>();
            if(opType.get().equals("dept")){
                tbOutputValues=countReportService.excelOutPutByDept(opYear.get(),opName.get());
            }else if(opType.get().equals("pro")){
                tbOutputValues=countReportService.excelOutPutByPro(opYear.get(),opName.get());
            }else{
                tbOutputValues=countReportService.excelOutPutByProLine(opYear.get(),opName.get());
            }
            DownloadExcelUtil downloadExcelUtil = null;
            String status="";
            try {
                String fileName = year + "产值统计明细" + ".xls";
                downloadExcelUtil = new DownloadExcelUtil(response, fileName, "产值统计");
                DateFormat dateFormat = new DateFormat("yyyy-mm-dd");
                downloadExcelUtil.addCell(0, 0, "合同名", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(1, 0, "产品名称", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(2, 0, "产品线", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(3, 0, "部门名称", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(4, 0, "阶段报告", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(5, 0, "核算比例", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(6, 0, "本次产值", CellType.LABEL, dateFormat, false, false);
                downloadExcelUtil.addCell(7, 0, "是否确认产值", CellType.LABEL, dateFormat, false, false);
                for (int i = 0; i < tbOutputValues.size(); i++) {
                    if(tbOutputValues.get(i).getHtName()==null){
                        downloadExcelUtil.addCell(0,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(0,i+1,tbOutputValues.get(i).getHtName(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getProName()==null){
                        downloadExcelUtil.addCell(1,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(1,i+1,tbOutputValues.get(i).getProName(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getProLine()==null){
                        downloadExcelUtil.addCell(2,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(2,i+1,tbOutputValues.get(i).getProLine(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getDepName()==null){
                        downloadExcelUtil.addCell(3,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(3,i+1,tbOutputValues.get(i).getDepName(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getDocName()==null){
                        downloadExcelUtil.addCell(4,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(4,i+1,tbOutputValues.get(i).getDocName(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getOutput()==null){
                        downloadExcelUtil.addCell(5,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(5,i+1,tbOutputValues.get(i).getOutput(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getSubtotal()==null){
                        downloadExcelUtil.addCell(6,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        downloadExcelUtil.addCell(6,i+1,tbOutputValues.get(i).getSubtotal(),CellType.LABEL,dateFormat,false,false);
                    }

                    if(tbOutputValues.get(i).getStatus()==null){
                        downloadExcelUtil.addCell(7,i+1,"",CellType.LABEL,dateFormat,false,false);
                    }else{
                        if(tbOutputValues.get(i).getStatus()==0){
                            status="否";
                        }else{
                            status="是";
                        }
                        downloadExcelUtil.addCell(7,i+1,status,CellType.LABEL,dateFormat,false,false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    downloadExcelUtil.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
}
