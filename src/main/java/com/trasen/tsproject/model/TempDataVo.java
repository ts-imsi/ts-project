package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhangxiahui on 17/10/9.
 */
@Setter
@Getter
public class TempDataVo {

    public String name;

    public Integer length;

    public String value;

    public List<TempDataVo> voList;


}
