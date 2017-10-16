package com.trasen.tsproject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TbMsg {
    /**
     * 自增主键
     */
    private Integer pkid;

    /**
     * 消息类型，read：阅读类消息，todo:待办类消息
     */
    private String type;

    /**
     * 接收工号
     */
    private String workNum;

    /**
     * 接收姓名
     */
    private String name;

    /**
     * 发送人工号
     */
    private String sendWorkNum;

    /**
     * 发送人姓名
     */
    private String sendName;

    /**
     * 发送时间
     */
    private Date sengTime;

    /**
     * 流程名
     */
    private String processKey;

    /**
     * 流程ID
     */
    private String processId;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 消息状态，0:未处理；1:已处理
     */
    private Integer status;

    /**
     * 操作时间
     */
    private Date updated;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
    * 消息标题
    * */
    private String title;
}