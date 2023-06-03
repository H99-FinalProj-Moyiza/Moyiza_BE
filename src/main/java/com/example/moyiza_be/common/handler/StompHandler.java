package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.chat.entity.ChatJoinEntry;
import com.example.moyiza_be.chat.repository.ChatJoinEntryRepository;
import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.common.redis.RedisCacheService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.userDetails.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private final RedisCacheService redisCacheService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();
        System.out.println("headerAccessor.getCommand() = " + headerAccessor.getCommand());
        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())
//                StompCommand.DISCONNECT.equals(headerAccessor.getCommand()) ||
//                    StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())
        ) {
            System.out.println("headerAccessor.getDestination() = " + headerAccessor.getDestination());
            return message;
        }

        if(StompCommand.CONNECT.equals(headerAccessor.getCommand())){
            String bearerToken = headerAccessor.getFirstNativeHeader("ACCESS_TOKEN");
            String token = jwtUtil.removePrefix(bearerToken);
            if(!jwtUtil.validateToken(token)){
                throw new IllegalArgumentException("토큰이 유효하지 않습니다");
            }
            Claims claims = jwtUtil.getClaimsFromToken(token);
            ChatUserPrincipal userInfo;
            try{
                System.out.println("headerAccessor.getDestination().toString() = " + headerAccessor.getDestination());
//                Long subscribedChatId = getChatIdFromDestination(headerAccessor.getDestination());
                userInfo = new ChatUserPrincipal(
                        Long.valueOf(claims.get("userId").toString()),
                        claims.get("nickName").toString(),
                        claims.get("profileUrl").toString(),
                        -1L
                );
            } catch(RuntimeException e){
                log.info("채팅 : 토큰에서 유저정보를 가져올 수 없음");
                throw new NullPointerException("chat : 유저정보를 읽을 수 없습니다");
            }
            redisCacheService.saveUserInfoToCache(sessionId, userInfo);
        }

        if(StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())){
            System.out.println("headerAccessor.getDestination() = " + headerAccessor.getDestination());

//            ChatUserPrincipal userInfo = redisCacheService.getUserInfoFromCache(sessionId);
//            ChatJoinEntry joinEntry =
//                    chatService.loadChatJoinEntryByUserIdAndChatId(userInfo.getUserId())

        }

        return message;
    }

    private Long getChatIdFromDestination(String destination){
        return null;
    }
}
