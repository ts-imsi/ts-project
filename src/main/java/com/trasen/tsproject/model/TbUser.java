package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/11/24
 */
@Getter
@Setter
public class TbUser {
    private Integer pkid;
    private String name;// '用户名',
    private String password;// '密码',
    private String displayName;// '展示名',
    private String perId;// '人员ID',
    private String xtoken;
    private String newPassword;
}
