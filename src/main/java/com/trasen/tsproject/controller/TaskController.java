package com.trasen.tsproject.controller;

import cn.trasen.core.entity.Result;
import com.trasen.tsproject.service.TbProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/1
 */
@RestController
@RequestMapping(value="/task")
public class TaskController {

    private static final Logger logger = Logger.getLogger(TaskController.class);

    @Autowired
    private TbProductService tbProductService;

    @RequestMapping(value = "/saveOrUpdateProductList", method = RequestMethod.GET, produces = "application/json")
    public Result saveOrUpdateProductList() {
        Result result = new Result();
        logger.info("======================同步产品数据开始");
        tbProductService.saveOrUpdateProductList();
        result.setSuccess(true);
        result.setStatusCode(1);
        result.setMessage("======================同步产品数据结束");
        return result;
    }
}
