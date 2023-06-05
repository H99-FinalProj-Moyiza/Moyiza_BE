package com.example.moyiza_be.config;

import com.example.moyiza_be.common.handler.ChatErrorHandler;
import com.example.moyiza_be.common.handler.MoyizaHandshakeInterceptor;
import com.example.moyiza_be.common.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler; // jwt 인증
    private final MoyizaHandshakeInterceptor moyizaHandshakeInterceptor;
    private final ChatErrorHandler chatErrorHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/chat");
        config.setApplicationDestinationPrefixes("/app");  // 유저가 보낼때 // app/보낼주소
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/connect")
                .setAllowedOriginPatterns("*")
//                .addInterceptors(moyizaHandshakeInterceptor)
                .withSockJS();
        registry.setErrorHandler(chatErrorHandler);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}