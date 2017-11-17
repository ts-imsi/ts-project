package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class TwfStageDoc {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档id
     */
    private Integer stageId;

    /**
     * 产值占比
     */
    private Double output;

    /**
     * 是否计算产值占比
     */
    private Integer isOutput;

    private Integer px;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 是否有效，1：有效，0：无效
     */
    private Integer isVaild;

    /**
     * 更新时间
     */
    private Date updated;

}