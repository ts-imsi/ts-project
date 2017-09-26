package com.trasen.tsproject.model;

import javax.persistence.*;

@Table(name = "tb_template_item")
public class TbTemplateItem {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkid;

    /**
     * 模板元素名
     */
    private String name;

    /**
     * 元素类型（handover：交接单）
     */
    private String type;

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
    @Column(name = "is_vaild")
    private Integer isVaild;

    /**
     * 级别，生成打印表格时使用
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer px;

    /**
     * 获取主键
     *
     * @return pkid - 主键
     */
    public Integer getPkid() {
        return pkid;
    }

    /**
     * 设置主键
     *
     * @param pkid 主键
     */
    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    /**
     * 获取模板元素名
     *
     * @return name - 模板元素名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板元素名
     *
     * @param name 模板元素名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取元素类型（handover：交接单）
     *
     * @return type - 元素类型（handover：交接单）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置元素类型（handover：交接单）
     *
     * @param type 元素类型（handover：交接单）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取元素展示格式（text|radio|textarea）
     *
     * @return input - 元素展示格式（text|radio|textarea）
     */
    public String getInput() {
        return input;
    }

    /**
     * 设置元素展示格式（text|radio|textarea）
     *
     * @param input 元素展示格式（text|radio|textarea）
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * 获取表单文本框长度（页面渲染样式用）
     *
     * @return length - 表单文本框长度（页面渲染样式用）
     */
    public Integer getLength() {
        return length;
    }

    /**
     * 设置表单文本框长度（页面渲染样式用）
     *
     * @param length 表单文本框长度（页面渲染样式用）
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 获取模块
     *
     * @return module - 模块
     */
    public String getModule() {
        return module;
    }

    /**
     * 设置模块
     *
     * @param module 模块
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 获取是否有效
     *
     * @return is_vaild - 是否有效
     */
    public Integer getIsVaild() {
        return isVaild;
    }

    /**
     * 设置是否有效
     *
     * @param isVaild 是否有效
     */
    public void setIsVaild(Integer isVaild) {
        this.isVaild = isVaild;
    }

    /**
     * 获取级别，生成打印表格时使用
     *
     * @return level - 级别，生成打印表格时使用
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置级别，生成打印表格时使用
     *
     * @param level 级别，生成打印表格时使用
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取排序
     *
     * @return px - 排序
     */
    public Integer getPx() {
        return px;
    }

    /**
     * 设置排序
     *
     * @param px 排序
     */
    public void setPx(Integer px) {
        this.px = px;
    }
}