package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.model.TbUser;
import com.trasen.tsproject.service.TbPersonnelService;
import com.trasen.tsproject.util.HttpUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

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
        String imitationLogin=env.getProperty("imitation_login");
        TbUser user=new TbUser();
        if(Optional.ofNullable(imitationLogin).isPresent()&&Optional.ofNullable(openId).isPresent()){
            TbUser tbUser=tbPersonnelService.selectTbuserByOpenId(openId);
            String parameterJson = JSONObject.toJSONString(tbUser);
            String json= HttpUtil.connectURL(imitationLogin,parameterJson,"POST");
            JSONObject dataJson = (JSONObject) JSONObject.parse(json);
            if(dataJson.getBoolean("success")){
                user=dataJson.getObject("object",TbUser.class);
            }
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

    @RequestMapping(value="/oauth2/{code}",method = RequestMethod.GET)
    public Map<String,Object> oauth2(@PathVariable String code){
        Map<String,Object> result=new HashMap<>();
        try{
            String imitationOpenid=env.getProperty("imitation_openid");

            if(Optional.ofNullable(imitationOpenid).isPresent()&&Optional.ofNullable(code).isPresent()){
                imitationOpenid=imitationOpenid.replace("{code}",code);
                StringBuffer str = new StringBuffer();
                URL url = new URL(imitationOpenid);
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setConnectTimeout(1000 * 60 * 5);
                urlConn.setReadTimeout(1000 * 60 * 5);
                urlConn.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"utf-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    str.append(line);
                }
                in.close();
                if (str.equals("") || str == null) {
                    System.err.println("openId失败");
                    return null;
                }
                System.out.println(str.toString());
                JSONObject dataJson =(JSONObject)JSONObject.parse(str.toString());
                if(dataJson.getInteger("status")==1){
                    String openId=dataJson.getString("openid");
                    result.put("openId",openId);
                    result.put("success",true);
                }else{
                    result.put("message","数据查询失败");
                    result.put("success",false);
                }
            }else{
                result.put("message","传入参数错误");
                result.put("success",false);
            }
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.put("message","数据查询失败");
            result.put("success",false);
        }
        return result;
    }
}
