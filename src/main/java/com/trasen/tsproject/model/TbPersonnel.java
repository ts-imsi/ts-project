package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/7/6
 */

@Getter
@Setter
public class TbPersonnel {
    private String perId; //人员ID
    private String workNum; //工号
    private String name; //姓名
    private Integer sex; //性别，1:男；2:女
    private String depId; //部门ID
    private String depName; //部门名称
    private String phone; //手机号码
    private Date created; //创建时间
    private Integer isVaild; //是否有效，1:有效；2:无效
    private String operator; //操作人
    private String tagCode;
    private String position;
    private Date updated;
    private String entryDate;//入职时间
    private String picture;
}
