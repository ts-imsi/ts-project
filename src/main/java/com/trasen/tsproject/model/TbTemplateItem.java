package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TbTemplateItem {
    /**
     * 主键
     */
    private Integer pkid;

    /**
     * 模板元素名
     */
    private String name;

    /**
     * 元素类型（handover：交接单）
     */
    private String type;

    private String code;

    private Integer isRequired;

    /**
     * 元素展示格式（text|radio|textarea）
     */
    private String input;

    /**
     * 表单文本框长度（页面渲染样式用）
     */
    private Integer length;

    /**
     * 模块
     */
    private String module;

    /**
     * 是否有效
     */
    private Integer isVaild;

    /**
     * 级别，生成打印表格时使用
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer px;


}