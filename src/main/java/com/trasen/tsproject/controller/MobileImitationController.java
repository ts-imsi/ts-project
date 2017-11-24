package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.alibaba.fastjson.JSONObject;
import com.trasen.tsproject.model.TbUser;
import com.trasen.tsproject.service.TbPersonnelService;
import com.trasen.tsproject.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

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
}
