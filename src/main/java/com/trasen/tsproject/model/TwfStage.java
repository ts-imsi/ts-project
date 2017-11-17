package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TwfStage {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 阶段名称
     */
    private String name;

    /**
     * 进度
     */
    private Double poit;

    private Integer px;

    private Date created;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 是否有效.1:有效，0：无效
     */
    private Integer isVaild;

    /**
     * 更新时间
     */
    private Date updated;

}