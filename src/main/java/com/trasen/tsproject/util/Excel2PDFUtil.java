package com.trasen.tsproject.util;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.trasen.tsproject.controller.FileUploadController;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by zhangxiahui on 17/11/24.
 */
public class Excel2PDFUtil {

    private static InputStream license;

    private static Logger logger = Logger.getLogger(Excel2PDFUtil.class);

    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            //ClassLoader loader = Thread.currentThread().getContextClassLoader();
            //license = new FileInputStream(loader.getResource("license.xml").getPath());// 凭证文件
            license = PropertiesUtils.getResourceAsStream("license.xml");
            logger.info("============excel转换PDF获取PDF许可证文件路径======");
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            logger.info("===excel转换PDF异常:"+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 支持DOC, DOCX, OOXML, RTF, HTML, OpenDocument, PDF, EPUB, XPS, SWF等相互转换<br>
     *
     * @param
     */
    public static boolean execute(String inputUrl,String outputUrl) {
        // 验证License
        if (!getLicense()) {
            return false;
        }

        try {
            long old = System.currentTimeMillis();
            Workbook wb = new Workbook(inputUrl);// 原始excel路径
            File pdfFile = new File(outputUrl);// 输出路径
            FileOutputStream fileOS = new FileOutputStream(pdfFile);
            wb.save(fileOS, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            System.out.println("excel转pdf共耗时：" + ((now - old) / 1000.0) + "秒");
            return true;
        } catch (Exception e) {
            logger.info("===excel转换PDF异常:"+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
