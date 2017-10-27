package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/25
 */
@Setter
@Getter
public class ContractInfo {
    private Integer id;

    private String contractNo; //合同编号

    private Integer contractType;//合同类型

    private String contractName;//合同名称

    private Integer contractPrice;//合同金额

    private Integer productType;//产品分类(1=软件，2=硬件，3=系统集成，4=服务，5=其他)

    private Integer buyPrice;//外购支付

    private Date signDate;//签约时间

    private String contractOwner;//合同所有者

    private String buyer;//甲方

    private String seller;//乙方

    private String buyerSigner;//甲方签约人

    private String sellerSigner;//乙方签约人

    private Integer customerId;//客户ID

    private Date createTime;//创建时间

    private Date timeLimit;//完成时限

    private Integer state;//状态(0未执行1执行中2未寄回4已结束)

    private String paymode;//付款方式

    private String remark;//备注

    private Integer createType;//创建类型，0=内控创建，1=销售创建

    private String createUserId;//创建用户

    private Date distributeDate;//分解时间

    private Date maintainLimit;//维护期限

    private String customerName;//客户名称

    private String customerNo;//客户编号

    private String hospitalGrade;//医院等级

    private String contactAddress;//医院地址

    private String contactPhone;//联系电话

    private String type;

    private String changeNo;

}
