package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhangxiahui on 17/10/24.
 */
@Setter
@Getter
public class TimeLineVo {

    private String taskId;
    private String taskKey;
    private String TaskName;
    private String assignee;
    private String startTime;
    private String endTime;
    private String deleteReason;
    private String title;  //TbMsg title
    private String remark;
    private String name;  //TbMsg name
    private String time;
    private String colour;




}
