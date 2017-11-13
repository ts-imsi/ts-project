package com.trasen.tsproject.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by zhangxiahui on 17/11/10.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //注册代理,这句表示在topic和user这两个域上可以向客户端发消息；
        config.enableSimpleBroker("/topic","/user");

        //这句表示客户端向服务端发送时的主题上面需要加"/app"作为前缀；
        //全局使用的订阅前缀（客户端订阅路径上会体现出来）
        config.setApplicationDestinationPrefixes("/app");

        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        //这句表示给指定用户发送（一对一）的主题前缀是“/user/”;
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //解决跨域问题
        //这个和客户端创建连接时的url有关，后面在客户端的代码中可以看到。
        registry.addEndpoint("/tsWebSocket").setAllowedOrigins("*").withSockJS();
    }

    @Bean
    public SocketSessionRegistry SocketSessionRegistry(){
        return new SocketSessionRegistry();
    }
    @Bean
    public STOMPConnectEventListener STOMPConnectEventListener(){
        return new STOMPConnectEventListener();
    }

}
