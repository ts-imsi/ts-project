package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class TbOutputValue {
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
     * 确认状态，0:未确认，1:已确认
     */
    private Integer status;

    /**
     * 产品编号
     */
    private String proCode;

    /**
     * 产品名称
     */
    private String proName;

    /**
     * 阶段报告ID
     */
    private Integer itemId;

    /**
     * 阶段报告
     */
    private String docName;

    /**
     * 核算比例
     */
    private Double output;

    /**
     * 本次产值
     */
    private Double subtotal;

    /**
     * 分配合同额
     */
    private Double total;

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

    private String depId;

    private String depName;

    @Transient
    private boolean checked = false;

    private String proLine;

    private String remark;

    @Transient
    private List<TbOutputValue> productList;


}