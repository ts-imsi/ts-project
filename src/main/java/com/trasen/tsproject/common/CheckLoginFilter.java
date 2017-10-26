package com.trasen.tsproject.common;

import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.util.PropertiesUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
public class CheckLoginFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(CheckLoginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();
        String xToken = null ;
        xToken = SecurityCheck.getHeaderValue(request,"X-TOKEN");


        String sign = null;
        String name = null;
        String pwd = null;
        String showName = null;
        String userId = null;
        Map<String, String> parameters = new HashedMap();
        if(xToken!=null){
            String [] token = xToken.split("\\.");
            if(token.length==2){
                sign = token[0];
                String json = new String(Base64.getDecoder().decode(token[1]),"utf-8");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(json);
                name = jsonObject.getString("name");
                pwd = jsonObject.getString("pwd");
                showName = jsonObject.getString("showName");
                userId = jsonObject.getString("userId");
                parameters.put("name", name);
                parameters.put("pwd", pwd);
                parameters.put("showName", showName);
                parameters.put("userId",userId);
            }
        }
        VisitInfoHolder.setUserId(userId);
        VisitInfoHolder.setShowName(showName);

        //FILTER_URL
        String filterUrl = PropertiesUtils.getProperty("FILTER_URL");
        boolean boo = false;
        if(filterUrl!=null){
            String[] urls  = filterUrl.split(",");
            for(String url : urls){
                if(requestUri.contains(url)){
                    boo = true;
                    break;
                }
            }
        }
        if(requestUri.equals("/")){
            boo = true;
        }
        if(boo){
            filterChain.doFilter(servletRequest, servletResponse);
        }else if("contentDev".equals(xToken)){
            logger.debug("进入登录系统Filter,开发者进入系统");
            filterChain.doFilter(servletRequest, servletResponse);
        }else if(SecurityCheck.checkSigner(parameters,sign)){//如果有签名先校验签名
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            //LOGGER.info("进入登录系统Filter,["+requestUri+"]["+xToken+"]无权限");
            request.setAttribute("msg", "您登录未授权");
            request.getRequestDispatcher("/fail.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
