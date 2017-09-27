package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/9/27
 */
@Getter
@Setter
public class TbStandardPrice {
    private Integer pkid;
    private String productId; //产品
    private String hospitalLevel; //医院等级
    private String standardPrice; //标准价
}
