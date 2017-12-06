package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author luoyun
 * @ClassName: IntelliJ IDEA
 * @Description: 操作类型
 * @date 2017/12/6
 */
@Getter
@Setter
public class TreeVo {
    private String label;

    private List<TreeVo> children;

    private Object data;
}
