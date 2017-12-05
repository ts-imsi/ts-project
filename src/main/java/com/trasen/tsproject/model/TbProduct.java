package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TbProduct {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 产品名称
     */
    private String proName;

    /**
     * 产品编号
     */
    private String proCode;

    /**
     * 部门id
     */
    private String depId;

    /**
     * 部门名称
     */
    private String depName;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 是否有效，1:有效；0:无效
     */
    private Integer isVaild;

    @Transient
    private List<TbProModule> proModuleList;

    private String proLine;
}