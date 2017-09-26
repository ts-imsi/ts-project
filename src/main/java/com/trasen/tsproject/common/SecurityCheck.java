package com.trasen.tsproject.common;

import com.trasen.tsproject.util.PropertiesUtils;
import com.trasen.tsproject.util.SignConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/7/26.
 */
public class SecurityCheck {

    private static final Logger logger = Logger.getLogger(SecurityCheck.class);

    public static String getCookieValue(HttpServletRequest request, String name) {
        String value = null;
        if(request != null && request.getCookies() != null) {
            for(Cookie ck : request.getCookies()) {
                if(ck.getName().contains(name)) {
                    value = ck.getValue();
                }
            }
        }
        return value;
    }

    public static String getHeaderValue(HttpServletRequest request, String name) {
        String value = null;
        if(request != null) {
            value = request.getHeader(name);
        }
        return value;
    }

    public static boolean checkSigner(Map<String, String> parameters, String contentSign) {
        boolean alongBoo = false;
        String sign = "";
        String secret = PropertiesUtils.getProperty("CONTENT_SECRET");
        if(parameters != null && contentSign != null) {
            try {
                sign = SignConvertUtil.generateMD5Sign(secret, parameters);
            } catch(NoSuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
            } catch(UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
            if(contentSign.equals(sign)) {
                //鉴权成功
                alongBoo = true;
            }
        }
        return alongBoo;
    }



    private static String getSign(String accessUrl, String callerService, String serviceSecret, String timestamp) {
        String[] strs = StringUtils.split(accessUrl, "/");
        //上下文
        String contextPath = strs[2];
        //版本号
        String version = strs[3];
        //        String[] requestParts = accessUrl.split("/\\d[.]\\d/");
        //请求路径
        String requestPath = StringUtils.substringAfter(accessUrl, version);

        return SignConvertUtil.generateSign(callerService, contextPath, version, timestamp, serviceSecret, requestPath);
    }

}
