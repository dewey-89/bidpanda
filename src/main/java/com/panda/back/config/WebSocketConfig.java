package com.panda.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP, Broker 사용 설정
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") // WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로이다.
                .setAllowedOriginPatterns("*") // front origin cors (일단 localhost로)
                .withSockJS();
    } // socket + STOMP (socket protocol)

    /**
     * /app 경로로 시작하는 STOMP 메시지의 destination 헤더는 @Controller 객체의 @MessageMapping 메서드로 라우팅 된다.
     * 내장된 메세지 브로커를 사용해 Client에게 Subscriptions, Broadcasting 기능을 제공한다. 또한 /topic, /queue로 시작하는 "destination" 헤더를 가진 메세지를 브로커로 라우팅한다.
     * 내장된 Simple Message Broker는 /topic, /queue prefix에 대해 특별한 의미를 부여하지 않는다.
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue"); // topic -> 메시지 쌓는 곳
    } // ws://djfksldf/app/chat/message/{recordId}
}
