package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
public class TbPlanCheck {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 计划ID
     */
    private Integer planId;

    /**
     * 计划详情ID
     */
    private Integer detailId;

    /**
     * 计划明细ID
     */
    private Integer itemId;

    /**
     * 确认人角色标签
     */
    private String checkTag;

    /**
     * 确认人角色名称
     */
    private String checkName;

    /**
     * 确认状态，0：未确认，1:已确认，2:驳回
     */
    private Integer status;

    /**
     * 权限
     */
    private String permission;

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