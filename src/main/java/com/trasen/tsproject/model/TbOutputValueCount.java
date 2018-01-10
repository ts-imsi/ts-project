package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Setter
@Getter
public class TbOutputValueCount {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 合同编号
     */
    private String htNo;

    /**
     * 合同名称
     */
    private String htName;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同签订日期
     */
    private String signDate;

    /**
     * 产品编号
     */
    private String proCode;

    /**
     * 产品名称
     */
    private String proName;

    /**
     * 产品线
     */
    private String proLine;

    /**
     * 部门名称
     */
    private String depName;

    /**
     * 部门ID
     */
    private String depId;

    /**
     * 分配合同额
     */
    private Double total;

    /**
     * 已完成产值
     */
    private Double finished;

    /**
     * 未完成产值
     */
    private Double unfinished;

    private String year;

    /**
     * 更新时间
     */
    private Date updated;


}