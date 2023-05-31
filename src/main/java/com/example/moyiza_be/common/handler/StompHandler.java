package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.chat.dto.ChatUserInfo;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String bearerToken = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

        if (bearerToken == null){
            throw new IllegalArgumentException("유저정보를 찾을 수 없습니다");
        }
        String token = jwtUtil.removePrefix(bearerToken);

        if (!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }

        Claims claims = jwtUtil.getClaimsFromToken(token);
        ChatUserInfo userInfo;
        try{
            userInfo = new ChatUserInfo(
                    (Long) claims.get("userId"),
                    (String) claims.get("nickName"),
                    (String) claims.get("profileUrl")
            );
        } catch(RuntimeException e){
            log.info("채팅 : 토큰에서 유저정보를 가져올 수 없음");
            throw new NullPointerException("chat : 유저정보를 읽을 수 없습니다");
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null, null);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        return message;
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
