package com.trasen.tsproject.controller;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Created by zhangxiahui on 17/10/31.
 */
@RestController
@RequestMapping("download")
public class DownloadController {

    private Logger logger = Logger.getLogger(DownloadController.class);

    @Autowired
    private Environment env;

    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public void fileUpload(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileName") String fileName) throws Exception{
        //一个简单的鉴权
        /*String userSign = SecurityCheck.getCookieValue(request,"userSign");
        logger.info("userSign鉴权===获取到userSign[" + userSign + "]");
        if(userSign==null){
            return;
        }
        if(!SecurityCheck.checkUserSigner(userSign)){
            return;
        }*/
        String filePath = env.getProperty("saveFileUrl");
        File file = new File(filePath+fileName);
        if (!file.exists()) {
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        OutputStream outputStream = response.getOutputStream();
        IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
    }



    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public void view(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileName") String fileName) throws Exception{
        //一个简单的鉴权
        /*String userSign = SecurityCheck.getCookieValue(request,"userSign");
        logger.info("userSign鉴权===获取到userSign[" + userSign + "]");
        if(userSign==null){
            return;
        }
        if(!SecurityCheck.checkUserSigner(userSign)){
            return;
        }*/
        String filePath = env.getProperty("saveFileUrl");
        String pdfName = fileName.split("\\.")[0]+".pdf";
        File file = new File(filePath+pdfName);
        if (!file.exists()) {
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setContentType("multipart/form-data");
        OutputStream outputStream = response.getOutputStream();
        IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
    }
}
