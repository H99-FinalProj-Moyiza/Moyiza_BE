package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;


//        System.out.println("bearerToken = " + bearerToken);
//        if (bearerToken == null){
//            throw new IllegalArgumentException("유저정보를 찾을 수 없습니다");
//        }
//
//        if (!jwtUtil.validateToken(jwtUtil.removePrefix(bearerToken))){
//            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
//        }
//        return message;

    }
//
//    @Override
//    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        ChannelInterceptor.super.postSend(message, channel, sent);
//    }
//
//    @Override
//    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
//    }
//
//    @Override
//    public boolean preReceive(MessageChannel channel) {
//        return ChannelInterceptor.super.preReceive(channel);
//}
//
//    @Override
//    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
//        return ChannelInterceptor.super.postReceive(message, channel);
//    }
//
//    @Override
//    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
//    }
}
