package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/12/6
 */
@Getter
@Setter
public class TbTree {
    private String pkid;
    private String parent;
    private String name;
    private String code;
    private Integer level;
    private String type;
    private Integer  is_final;
}
