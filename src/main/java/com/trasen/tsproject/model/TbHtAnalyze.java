package com.trasen.tsproject.model;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;


@Getter
@Setter
public class TbHtAnalyze {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 合同编号
     */
    private String htNo;

    /**
     * 交接单id
     */
    private String handoverId;

    /**
     * 部门id
     */
    private String depId;

    /**
     * 产品code
     */
    private String proCode;

    /**
     * 负责人
     */
    private String operator;

    /**
     * 状态
     */
    private Integer status;


    /**
    * 产品名称
    * */
    private String proName;

    /**
    * 部门名称
    * */
    private String depName;


    @Transient
    private List<Select> selectJson;

    @Transient
    private List<Select> data;
}