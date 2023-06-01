package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    @Order(Ordered.HIGHEST_PRECEDENCE + 99)
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("check0 -> headerAccessor.getCommand() = " + headerAccessor.getCommand());
        if(StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) ||
                StompCommand.DISCONNECT.equals(headerAccessor.getCommand()) ||
                        StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())
        ){
            return message;
        }


        String bearerToken = String.valueOf(headerAccessor.getNativeHeader("ACCESS_TOKEN"))
                        .replaceAll("[\\[\\]]","");  // token 앞뒤의 []를 제거
        System.out.println("check1 -> bearerToken = " + bearerToken);


        if (bearerToken.equals("null")){
            throw new IllegalArgumentException("유저정보를 찾을 수 없습니다");
        }
        String token = jwtUtil.removePrefix(bearerToken);
        System.out.println("check2 -> token = " + token);

        if (!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }

        Claims claims = jwtUtil.getClaimsFromToken(token);
        System.out.println("check3 -> claims.get(\"userId\") = " + claims.get("userId"));
        System.out.println("claims.get(\"userId\").toString() = " + claims.get("userId").toString());
        System.out.println("(Long) claims.toString() = " + Long.valueOf(claims.get("userId").toString()));
        System.out.println("check3 -> claims.get(\"nickName\") = " + claims.get("nickName"));
        System.out.println("claims.get(\"profileUrl\") = " + claims.get("profileUrl"));

        ChatUserPrincipal userPrincipal;
        try{
            userPrincipal = new ChatUserPrincipal(
                    Long.valueOf(claims.get("userId").toString()),
                    claims.get("nickName").toString(),
                    claims.get("profileUrl").toString()
            );
        } catch(RuntimeException e){
            log.info("채팅 : 토큰에서 유저정보를 가져올 수 없음");
            throw new NullPointerException("chat : 유저정보를 읽을 수 없습니다");
        }

//        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal,null, null);

//        headerAccessor.setUser(authentication);
//        headerAccessor.setHeader("auth", userPrincipal);

        System.out.println("handler headerAccessor = " + headerAccessor);
        System.out.println("handler headerAccessor.getUser() = " + headerAccessor.getUser());
        System.out.println("((ChatUserPrincipal) headerAccessor.getUser()) = " + ((ChatUserPrincipal) headerAccessor.getUser()));


//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null, null);
//        System.out.println("check4 -> authentication.isAuthenticated() = " + authentication.isAuthenticated());
//        System.out.println("check4 -> authentication.getPrincipal() = " + authentication.getPrincipal());
//        System.out.println("(authentication.getPrincipal() instanceof ChatUserInfo) = " + (authentication.getPrincipal() instanceof ChatUserInfo));
//        context.setAuthentication(authentication);
//        SecurityContextHolder.setContext(context);
//
//        System.out.println("SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof  ChatUserInfo = " + (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof  ChatUserInfo));

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
