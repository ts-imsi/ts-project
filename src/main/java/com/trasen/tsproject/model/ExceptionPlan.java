package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/20
 */
@Getter
@Setter
public class ExceptionPlan {

    private Integer pkid;

    /**
     * 计划id
     */
    private Integer planId;
    /**
     * 合同名称
     */
    private String htNo;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 产品code
     */
    private String proCode;
    /**
     * 产品名称
     */
    private String proName;
    /**
     * 实施经理
     */
    private String actualizeManager;
    /**
     * 阶段名称
     */
    private String stageName;
    /**
     * 文档名称
     */
    private String docName;

    /**
     * 计划时间
     */
    private String planTime;

    /**
     * 是否提交，0:为提交，1:已提交
     */
    private Integer isSubmit;
}
