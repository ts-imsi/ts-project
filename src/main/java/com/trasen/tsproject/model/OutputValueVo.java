package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by zhangxiahui on 17/11/30.
 */
@Setter
@Getter
public class OutputValueVo {

    private String id;

    private String name;

    private Double totle;

    private List<TbOutputValue> outputValueList;

    @Transient
    private boolean ifOpen = false;

    @Transient
    private boolean checked = false;


}
