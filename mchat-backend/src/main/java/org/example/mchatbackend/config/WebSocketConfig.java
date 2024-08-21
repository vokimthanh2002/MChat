package org.example.mchatbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) { //cho phep ban cau hinh message broker trong ung dung cua minh
        config.enableSimpleBroker("/topic"); //Phan phoi message toi cilent dang ky vao cac topic nhat dinh
        config.setApplicationDestinationPrefixes("/app"); //Thiet lap mot tien to (prefix) cho cac destination cuar message gui tu cilent den server.
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //dang ki cac STOMP endpoint, de ket noi server qua Websocket
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
//        registry.addEndpoint("/ws").withSockJS(); // Địa chỉ endpoint để kết nối WebSocket
    }
}