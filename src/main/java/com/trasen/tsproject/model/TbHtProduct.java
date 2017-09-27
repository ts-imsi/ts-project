package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TbHtProduct {
    private Integer pkid;

    /**
     * 合同编号
     */
    private String htNo;

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 产值
     */
    private String outputValue;

    /**
     * 部门id
     */
    private String depId;

    /**
     * 小计
     */
    private Double subtotal;

    /**
     * 分解
     */
    private String decompose;

    /**
    * 产品列表
    */
    private String productName;

    /**
     * 标准价
     */
    private String standardPrice;

    /**
     * 部门名称
     */
    private String depName;

}