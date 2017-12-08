package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TbHtResolve {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     *合同编号
     */
    private String htNo;

    /**
     * 产品ID
     */
    private String proCode;

    /**
     * 模块累计价格
     */
    private Double price;

    /**
     * 产品小计
     */
    private Double subtotal;

    private Double total;

    /**
     * 占比
     */
    private String outputValue;

    /**
     * 分解负责人
     */
    private String proMan;

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

    private String depName;

    private String depId;

    private String proName;

    private String htPrice;

}