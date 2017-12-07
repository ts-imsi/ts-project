package com.trasen.tsproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.common.SecurityCheck;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangxiahui on 17/8/21.
 * 接口调试用,测试
 */
@RestController
@RequestMapping(value="/ts-authorize")
public class AuthorizeController {

    Logger logger = Logger.getLogger(AuthorizeController.class);

    @Autowired
    private Environment env;

    String xToken = "71FD532E9C8D4128D670867D709F8172.eyJuYW1lIjoidHMtaW1pcyIsInB3ZCI6IjEyMzMyMSIsInNob3dOYW1lIjoi6YKT5paH54G/IiwidXNlcklkIjoiMyJ9";


    @RequestMapping(value = "/{appId}/menus", method = RequestMethod.GET)
    public JSONObject getMenus(@PathVariable String appId, HttpServletRequest request) {
        logger.info("===权限系统:获取菜单权限====["+ VisitInfoHolder.getShowName()+"]====");
        //String xToken = SecurityCheck.getHeaderValue(request,"X-TOKEN");
        String menusUrl = env.getProperty("authorize_menus").replace("{appId}",appId);
        String result = HttpUtil.connectURLGET(menusUrl,xToken);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject;
    }

    @RequestMapping(value = "/{appId}/operList/{state}", method = RequestMethod.GET)
    public JSONObject getOperList(@PathVariable String appId, @PathVariable String state, HttpServletRequest request) {
        logger.info("===权限系统:获取页面操作权限====["+ VisitInfoHolder.getShowName()+"]====["+state+"]===");
        //String xToken = SecurityCheck.getHeaderValue(request,"X-TOKEN");
        String menusUrl = env.getProperty("authorize_operList")
                .replace("{appId}",appId).replace("{state}",state);
        String result = HttpUtil.connectURLGET(menusUrl,xToken);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject;
    }






}
