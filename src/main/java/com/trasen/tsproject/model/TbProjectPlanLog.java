package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
public class TbProjectPlanLog {
    /**
     * 主键
     */
    private Integer pkid;

    /**
     * 计划id
     */
    private Integer planId;

    /**
     * 变更前时间
     */
    private String oldTime;

    /**
     * 变更后时间
     */
    private String newTime;

    /**
     * 调研:survey;进场:approac;上线:online;验收:check
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 变更人
     */
    private String operator;


}