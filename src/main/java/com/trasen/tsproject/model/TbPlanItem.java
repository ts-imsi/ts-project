package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TbPlanItem {
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
     * 阶段名称
     */
    private String stageName;

    /**
     * 进度
     */
    private Double poit;

    /**
     * 阶段提交文档
     */
    private String docName;

    /**
     * 产值比例
     */
    private Double output;

    /**
     * 是否计算产值，0:否，1:是
     */
    private Integer isOutput;

    /**
     * 计划时间
     */
    private String planTime;

    private Integer isUpdate;



    /**
     * 是否完成，0:未完成，1:完成
     */
    private Integer isComplete;

    /**
     * 提交人
     */
    private String submitter;

    /**
     * 是否提交，0:为提交，1:已提交
     */
    private Integer isSubmit;

    /**
     * 文档提交时间
     */
    private Date submitTime;

    private String fileName;

    /**
     * 评分
     */
    private Integer score;

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

    private String role;


    @Transient
    private String remark;

    @Transient
    private List<TbPlanCheck> planChecks;

    @Transient
    private String userRole;

    @Transient
    private boolean isTime = false;




}