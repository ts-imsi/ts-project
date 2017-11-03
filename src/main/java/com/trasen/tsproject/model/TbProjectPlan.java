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
    private String surveyTime;

    /**
     * 进场时间
     */
    private String approachTime;

    /**
     * 上线时间
     */
    private String onlineTime;

    /**
     * 验收时间
     */
    private String checkTime;

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

    private String remark;

}