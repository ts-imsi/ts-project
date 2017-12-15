package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhangxiahui on 17/12/15.
 */
@Getter
@Setter
public class ProModuleVo {

    String proName;

    List<String> modList;

    Integer size;

}
