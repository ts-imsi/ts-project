package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TbProModulePrice {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 模块ID
     */
    private String modId;

    /**
     * 医院等级
     */
    private String hospitalLevel;

    /**
     * 标准价
     */
    private Double standardPrice;

}