package com.trasen.tsproject.controller;

import com.trasen.tsproject.common.SocketSessionRegistry;
import com.trasen.tsproject.model.*;
import com.trasen.tsproject.service.TbMsgService;
import org.apache.log4j.Logger;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zhangxiahui on 17/11/10.
 */
@Controller
@EnableScheduling
public class WsController {

    private static final Logger logger = Logger.getLogger(WsController.class);


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



    @Scheduled(fixedRate = 5*60*1000)
    public void sendMsgCount() throws Exception {
        // 网客户端发送待办消息
        ConcurrentMap<String, Set<String>> allUser = webAgentSessionRegistry.getAllSessionIds();
        Set<String> userSet = allUser.keySet();
        logger.info("====webSocket发送给客户端消息,目前访问的客户端为["+userSet.size()+"]个==========");
        for(String userId : userSet){
            if(webAgentSessionRegistry.getSessionIds(userId)!=null&&webAgentSessionRegistry.getSessionIds(userId).size()>0){
                String sessionId=webAgentSessionRegistry.getSessionIds(userId).stream().findFirst().get();
                if(sessionId!=null){
                    Map<String,Object> map = tbMsgService.indexMsgCount(userId);
                    messagingTemplate.convertAndSendToUser(sessionId,"/topic/message",map,createHeaders(sessionId));
                }
            }
        }
    }

    /**
     * 用于页面测试Ddemo (请不要删除)
     * 同样的发送消息   只不过是ws版本  http请求不能访问
     * 根据用户key发送消息
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/msg/hellosingle")
    public void greeting2(InMessage message) throws Exception {
        String sessionId=webAgentSessionRegistry.getSessionIds(message.getId()).stream().findFirst().get();
        messagingTemplate.convertAndSendToUser(sessionId,"/topic/greetings",new OutMessage("single send to："+message.getId()+", from:" + message.getName() + "!"),createHeaders(sessionId));
        messagingTemplate.convertAndSendToUser(message.getId(),"/topic/greetings",new OutMessage("single send to："+message.getId()+", from:" + message.getName() + "!"));
        messagingTemplate.convertAndSend("/topic/"+message.getId()+"/callback", new OutMessage("single send to："+message.getId()+", from:" + message.getName() + "!"));


    }


    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
