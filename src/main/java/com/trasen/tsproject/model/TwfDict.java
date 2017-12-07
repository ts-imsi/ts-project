package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/12/7
 */
@Getter
@Setter
public class TwfDict {
    private int pkid;
    private String name;
    private String code;
    private int type;
    private int isVaild;
    private String remark;
}
