package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/27
 */
@Setter
@Getter
public class ContractProduct {
    private String contractNo;
    private String contractType;
    private String contractName;
    private String contractOwner;
    private Integer productId;
    private String productName;
    private String productNo;
    private String type;
    private String dicName;
    private Date createDate;
    private Integer versionCode;
    private Integer latest;
    private String deptId;
    private String name;
    private String imisid;
    private String imisname;
}
