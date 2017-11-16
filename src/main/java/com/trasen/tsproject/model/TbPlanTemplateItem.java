package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TbPlanTemplateItem {

    private Integer pkid;

    /**
     * 模板id
     */
    private Integer tempId;

    /**
     * 阶段id
     */
    private Integer stageId;

    /**
     * 文档id
     */
    private Integer stageDocId;

    private String role;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 操作人
     */
    private String operator;

}