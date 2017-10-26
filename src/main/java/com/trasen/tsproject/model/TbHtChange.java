package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TbHtChange {
    /**
     * 主键
     */
    private Integer pkid;

    /**
     * 合同编号
     */
    private String htNo;

    /**
     * 合同名称
     */
    private String htName;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 变更类型
     */
    private String type;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 合同拥有者
     */
    private String htOwner;

    /**
     * 合同签订日期
     */
    private String signDate;

    /**
     * 变更内容
     */
    private String changeContent;

    /**
     * 工号
     */
    private String workNum;

    /**
     * 申请部门
     */
    private String applicationDept;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date created;

    /**
    * 流程id
    * */
    private String processId;
}