package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.model.TbPersonnel;
import com.trasen.tsproject.service.TbPersonnelService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/24
 */
@RestController
@RequestMapping(value="/mobileUser")
public class MobileUserController {

    private static final Logger logger = Logger.getLogger(MobileUserController.class);

    @Autowired
    TbPersonnelService tbPersonnelService;

    @RequestMapping(value="/selectTbPersonnel",method = RequestMethod.POST)
    public Result selectTbPersonnel(){
        Result result=new Result();
        try{
            TbPersonnel tbPersonnel=tbPersonnelService.selectTbPersonnel();
            result.setSuccess(true);
            result.setObject(tbPersonnel);
        }catch (Exception e){
            logger.error("数据查询失败"+e.getMessage(),e);
            result.setSuccess(false);
            result.setMessage("数据查询失败");
        }
        return result;
    }
}
