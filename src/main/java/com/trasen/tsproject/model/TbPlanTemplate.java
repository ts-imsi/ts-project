package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class TbPlanTemplate {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 模板名称
     */
    private String tempName;

    /**
     * 产品code
     */
    private String proCode;

    /**
     * 产品名称
     */
    private String proName;

    /**
     * 模板类型
     */
    private String type;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 操作人
     */
    private String operator;
}