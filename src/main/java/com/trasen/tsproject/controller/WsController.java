package com.trasen.tsproject.controller;

import com.trasen.tsproject.common.SocketSessionRegistry;
import com.trasen.tsproject.model.RequestMessage;
import com.trasen.tsproject.model.ResponseMessage;
import com.trasen.tsproject.model.SocketMessage;
import com.trasen.tsproject.service.TbMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxiahui on 17/11/10.
 */
@Controller
@EnableScheduling
public class WsController {


    /**session操作类*/
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TbMsgService tbMsgService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @MessageMapping("/send")
    @SendTo("/topic/send")
    public SocketMessage send(SocketMessage message) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.date = df.format(new Date());
        return message;
    }

    //@Scheduled(fixedRate = 10000)
    @SendTo("/topic/callback")
    public Object callback() throws Exception {
        // 网客户端发送待办消息
        Map<String,Object> map = tbMsgService.indexMsgCount();
        messagingTemplate.convertAndSend("/topic/callback", map);
        return "callback";
    }



    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public ResponseMessage say(RequestMessage message) {
        System.out.println(message.getName());
        return new ResponseMessage("welcome," + message.getName() + " !");
    }



    @Scheduled(fixedRate = 10000)
    public void sendMsgCount() throws Exception {
        // 网客户端发送待办消息
        // TODO: 17/11/13 遍历订阅的客户掉,定时推送消息
        if(webAgentSessionRegistry.getSessionIds("3")!=null&&webAgentSessionRegistry.getSessionIds("3").size()>0){
            String sessionId=webAgentSessionRegistry.getSessionIds("3").stream().findFirst().get();

            if(sessionId!=null){
                Map<String,Object> map = tbMsgService.indexMsgCount("3");
                messagingTemplate.convertAndSendToUser(sessionId,"/queue/notifications",map,createHeaders(sessionId));
            }
        }



    }


    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
