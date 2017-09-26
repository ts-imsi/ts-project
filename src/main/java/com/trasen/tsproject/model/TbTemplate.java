package com.trasen.tsproject.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_template")
public class TbTemplate {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * 获取模板名
     *
     * @return name - 模板名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模板名
     *
     * @param name 模板名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取模板类型（handover：交接单）
     *
     * @return type - 模板类型（handover：交接单）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置模板类型（handover：交接单）
     *
     * @param type 模板类型（handover：交接单）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取更新时间
     *
     * @return updated - 更新时间
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * 设置更新时间
     *
     * @param updated 更新时间
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * 获取操作人
     *
     * @return operator - 操作人
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 设置操作人
     *
     * @param operator 操作人
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取模板元数据
     *
     * @return content - 模板元数据
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置模板元数据
     *
     * @param content 模板元数据
     */
    public void setContent(String content) {
        this.content = content;
    }
}