package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class TbProModule {
    /**
     * 自增主键
     */
    private String modId;

    /**
     * 模块名
     */
    private String modName;

    /**
     * 模块编号
     */
    private String modNo;

    /**
     * 产品编号
     */
    private String proCode;

    /**
     * 版本
     */
    private String version;

    /**
     * 是否有效，1:有效；0:无效
     */
    private Integer isVaild;

    /**
     * 创建时间
     */
    private Date created;



}