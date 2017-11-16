package com.trasen.tsproject.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwfCheckTag {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 名称
     */
    private String name;

    /**
     * 标签id
     */
    private String tagId;

    /**
     * 标签名字
     */
    private String tagName;

    /**
     * 是否有效:1：有效，0：无效
     */
    private Integer isVaild;

    /**
     * 备注
     */
    private String remark;

    /**
     * 权限
     */
    private String permission;
}