package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TbHtModuleChange {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 合同编号
     */
    private String htNo;

    /**
     * 产品ID
     */
    private String proCode;

    /**
     * 模块ID
     */
    private String modId;

    /**
     * 标准价
     */
    private Double price;

    /**
     * 修改时间
     */
    private Date updated;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作类型 old:老模块数据,new:新模块数据
     */
    private String type;

}