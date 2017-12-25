package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TbProjectManager {
    /**
     * 主键
     */
    private Integer pkid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String workNum;

    /**
     * 人员id
     */
    private String preId;

    /**
     * 类型 1：项目经理。2、实施经理
     */
    private String type;

    /**
     * 是否有效
     */
    private Integer isVaild;

    /**
     * 创建时间
     */
    private Date created;

    private String phone;

    private String email;

    private Integer sex;

}