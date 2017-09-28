package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TbTemplate {
    /**
     * 主键
     */
    private Integer pkid;

    /**
     * 模板名
     */
    private String name;

    /**
     * 模板类型（handover：交接单）
     */
    private String type;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 模板元数据
     */
    private String content;

    @Transient
    private List<TbTemplateItem> contentJson;


}