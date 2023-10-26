package com.panda.back.config;

import com.panda.back.domain.chat.handler.ChatInboundInterceptor;
import com.panda.back.domain.chat.handler.ChatOutboundInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // STOMP, Broker 사용 설정
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ChatInboundInterceptor chatInboundInterceptor;
    private final ChatOutboundInterceptor chatOutboundInterceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") // WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로이다.
                .setAllowedOriginPatterns("*") // front origin cors (일단 localhost로)
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue"); // topic -> 메시지 쌓는 곳
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatInboundInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatOutboundInterceptor);
    }
}
