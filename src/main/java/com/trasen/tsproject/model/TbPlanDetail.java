package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
public class TbPlanDetail {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 项目计划ID
     */
    private Integer planId;

    /**
     * 总评分
     */
    private Integer total;

    /**
     * 计划周期
     */
    private Integer planCycle;

    /**
     * 完成周期
     */
    private Integer cycle;

    /**
     * 提成基准金额
     */
    private Integer takeDase;

    /**
     * 提成系数（初）
     */
    private Double initTake;

    /**
     * 提成系数（终）
     */
    private Double endTake;

    /**
     * 难度系数（初始）
     */
    private Double initHard;

    /**
     * 难度系统（终）
     */
    private Double endHard;

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