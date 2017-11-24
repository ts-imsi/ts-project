package com.trasen.tsproject.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by zhangxiahui on 17/11/24.
 */
public class Work2PDFUtil {

    private static InputStream license;

    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {
        boolean result = false;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            license = new FileInputStream(loader.getResource("license.xml").getPath());// 凭证文件
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static boolean execute(InputStream fileInput,File outputFile) {
        // 验证License
        if (!getLicense()) {
            return false;
        }
        try {
            long old = System.currentTimeMillis();
            Document doc = new Document(fileInput);
            FileOutputStream fileOS = new FileOutputStream(outputFile);
            doc.save(fileOS, SaveFormat.PDF);
            long now = System.currentTimeMillis();
            System.out.println("word转pdf共耗时：" + ((now - old) / 1000.0) + "秒\n\n" + "文件保存在:" + outputFile.getPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
