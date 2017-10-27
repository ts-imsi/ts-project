package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TbHtHandover {
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
     * 交接单类型
     */
    private String type;

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
     * 是否分解
     */
    private Integer isRecount;

    /**
     * 是否确认
     */
    private Integer isConfirm;

    /**
     * 是否安排
     */
    private Integer isArrange;

    /**
     * 项目经理
     */
    private String proManager;

    /**
     * 项目经理电话
     */
    private String proPhone;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date updated;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 交接单内容，元数据
     */
    private String content;

    /**
    * 流程ID
    * */
    private String processId;

    private String nowStep;

    private String changeNo;

    @Transient
    private List<TbTemplateItem> contentJson;


}