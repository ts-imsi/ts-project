package com.trasen.tsproject.controller;

import com.trasen.tsproject.service.IndexService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2018/1/22
 */
@RequestMapping(value="/index")
@RestController
public class IndexController {
    private final  static Logger logger=Logger.getLogger(IndexController.class);

    @Autowired
    IndexService indexService;

    @RequestMapping(value="/getIndexMain",method = RequestMethod.POST)
    public Map<String,Object> getIndexMain(){
        Map<String,Object> result=new HashMap<>();
        try{
            result=indexService.getIndexMain();
            result.put("success",true);
        }catch (Exception e){
            result.put("message","数据查询失败");
            result.put("success",false);
        }
        return result;
    }
}
