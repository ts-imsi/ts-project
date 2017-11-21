package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhangxiahui on 17/11/21.
 */
@Getter
@Setter
public class TbPlanStage {

    /**
     * 阶段名称
     */
    private String stageName;


    private String planStartTime;

    private String planEndTime;


    private Double poit;


    private List<TbPlanItem> tbPlanItems;

    private boolean ifOpen = true;
}
