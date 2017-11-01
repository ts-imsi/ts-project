package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TbProjectPlan {
    /**
     * 主键
     */
    private Integer planId;

    /**
     * 交接单id
     */
    private String handoverId;

    /**
     * 交接单类型
     */
    private String handoverType;

    /**
     * 项目名称
     */
    private String proName;

    /**
     * 项目id
     */
    private String proCode;

    /**
     * 实施经理
     */
    private String actualizeManager;

    /**
     * 实施经理工号
     */
    private String workNum;

    /**
     * 调研时间
     */
    private Date surveyTime;

    /**
     * 进场时间
     */
    private Date approachTime;

    /**
     * 上线时间
     */
    private Date onlineTime;

    /**
     * 验收时间
     */
    private Date checkTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 变更时间
     */
    private Date updated;

}