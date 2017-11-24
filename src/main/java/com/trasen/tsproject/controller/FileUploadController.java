package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbPlanItem;
import com.trasen.tsproject.service.PlanDetailService;
import com.trasen.tsproject.util.Excel2PDFUtil;
import com.trasen.tsproject.util.Work2PDFUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/21.
 */
@RestController
@RequestMapping(value="fileUpload")
public class FileUploadController {


    private Logger logger = Logger.getLogger(FileUploadController.class);

    @Autowired
    private Environment env;

    @Autowired
    PlanDetailService planDetailService;


    /**
     * 基于用户标识的头像上传
     * @param file 图片
     * @param id
     * @return
     */
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("id") Integer id,@RequestParam("name") String name) {
        Result result=new Result();
        result.setSuccess(false);
        result.setMessage("上传失败");
        if (!file.isEmpty()) {
            if (file.getContentType().contains("doc")) {
                try {
                    // 获取图片的文件名
                    String fileName = file.getOriginalFilename();
                    // 获取图片的扩展名
                    String extensionName = StringUtils.substringAfter(fileName, ".");
                    // 新的图片文件名 = 获取时间戳+"."图片扩展名
                    String newFileName = id + "." + extensionName;
                    // 数据库保存的目录
                    // 文件路径
                    String filePath = env.getProperty("saveFileUrl");

                    File dest = new File(filePath, newFileName);
                    if (!dest.getParentFile().exists()) {
                        dest.getParentFile().mkdirs();
                    }
                    byte[] bytes = file.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(dest));
                    stream.write(bytes);
                    stream.close();

                    boolean boo = false;

                    if(extensionName.indexOf("doc")>-1){
                        boo = Work2PDFUtil.execute(new FileInputStream(filePath+newFileName),new File(filePath+id+".pdf"));
                    }

                    if(extensionName.indexOf("xls")>-1){
                        boo = Excel2PDFUtil.execute(filePath+newFileName,filePath+id+".pdf");
                    }

                    if(boo){
                        TbPlanItem item = new TbPlanItem();
                        item.setSubmitter(VisitInfoHolder.getShowName());
                        item.setPkid(id);
                        item.setFileName(newFileName);
                        planDetailService.updatePlanItemDocFile(item);
                        result.setStatusCode(1);
                        result.setMessage("上传成功!");
                    }
                } catch (IOException e) {
                    result.setStatusCode(0);
                    result.setMessage("上传失败!");
                }
            } else {
                result.setStatusCode(0);
                result.setMessage("上传的文件不是文档类型，请重新上传!");
            }
            return result;
        } else {
            result.setStatusCode(0);
            result.setMessage("上传失败，请选择要上传的文档!");
            return result;
        }
    }

}
