package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.trasen.tsproject.common.VisitInfoHolder;
import com.trasen.tsproject.model.TbUser;
import com.trasen.tsproject.service.TbPersonnelService;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/24
 */
@RestController
@RequestMapping(value="/mobileImitation")
public class MobileImitationController {

    @Autowired
    private Environment env;

    @Autowired
    TbPersonnelService tbPersonnelService;

    private static final Logger logger = Logger.getLogger(MobileImitationController.class);

    @RequestMapping(value="/imitationLogin/{openId}",method = RequestMethod.POST)
    public Result imitationLogin(@PathVariable String openId){
        Result result=new Result();
        TbUser user=new TbUser();
        if(Optional.ofNullable(openId).isPresent()){
            TbUser tbUser=tbPersonnelService.selectTbuserByOpenId(openId);
            user=tbPersonnelService.ctreateXToken(tbUser);
        }
        if(user==null){
            result.setMessage("用户名或密码有误");
            result.setSuccess(false);
            return result;
        }else{
            result.setSuccess(true);
            result.setObject(user);
            return result;
        }
    }

    @RequestMapping(value = "/authorize/oauth2", method = RequestMethod.GET)
    public JSONObject oauth2(@QueryParam("code") String code){
        logger.info("===主服务系统:获取OpenId====["+ code +"]===");
        String imitationOpenid=env.getProperty("imitation_openid");
        imitationOpenid=imitationOpenid.replace("{code}",code);
        String result = HttpUtil.connectURLGET(imitationOpenid);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject;
    }
}
