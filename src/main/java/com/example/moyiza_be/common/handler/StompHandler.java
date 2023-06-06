package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.common.redis.RedisCacheService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
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
        log.info("Command : " + headerAccessor.getCommand());
        if(StompCommand.CONNECT.equals(headerAccessor.getCommand())){
            String bearerToken = headerAccessor.getFirstNativeHeader("ACCESS_TOKEN");
            String token = jwtUtil.removePrefix(bearerToken);
            if(!jwtUtil.validateToken(token)){
                throw new IllegalArgumentException("토큰이 유효하지 않습니다");
            }
            Claims claims = jwtUtil.getClaimsFromToken(token);
            ChatUserPrincipal userInfo;
            try{
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
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())
        ) {
            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            String destination = headerAccessor.getDestination();
            Long chatId = getChatIdFromDestination(destination);
            userPrincipal.setSubscribedChatId(chatId);
            redisCacheService.saveUserInfoToCache(sessionId, userPrincipal);

            Long lastReadMessage = redisCacheService.getUserLastReadMessage(chatId.toString(), userPrincipal.getUserId().toString());
            if(lastReadMessage != null){
                log.info("Setting header for readcount :: user " + userPrincipal.getUserId() + "lastread message : " + lastReadMessage);
                headerAccessor.setHeader("lastRead", 123);
            }


            redisCacheService.removeUnsubscribedUser(chatId.toString(), userPrincipal.getUserId().toString());
            redisCacheService.addSubscriptionToChatId(chatId.toString(), sessionId);
//            ChatJoinEntry chatJoinEntry =
//                    chatJoinEntryRepository.findByUserIdAndChatIdAndIsCurrentlyJoinedTrue(userPrincipal.getUserId(), chatId)
//                                    .orElseThrow(() -> new NullPointerException("참여중인 채팅방이 아닙니다"));
            log.info("Before sending subscribe message");
            channel.send(message);
            log.info("After sending subscribe message");


            return message;
        }

        if(StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())){
            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            unsubscribe(userPrincipal, sessionId);
        }

        if(StompCommand.DISCONNECT.equals(headerAccessor.getCommand())){
            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            if(userPrincipal.getSubscribedChatId().equals(-1L)){
                log.info("User is not subscribed to any chat .. continue DISCONNECT");
            }
            else{
                unsubscribe(userPrincipal,sessionId);
            }
            redisCacheService.deleteUserInfoFromCache(sessionId);
        }

        return message;
    }

    private Long getChatIdFromDestination(String destination){
        if(destination == null){
            throw new NullPointerException("subscribe destination not defined");
        }
        return Long.valueOf(destination.replaceAll("\\D", ""));
    }
    public void unsubscribe(ChatUserPrincipal userPrincipal, String sessionId){
        log.info(userPrincipal.getUserId() + " unsubscribing chatroom " + userPrincipal.getSubscribedChatId());
        Long chatId = userPrincipal.getSubscribedChatId();
//        ChatJoinEntry chatJoinEntry =
//                chatJoinEntryRepository.findByUserIdAndChatIdAndIsCurrentlyJoinedTrue
//                                (userPrincipal.getUserId(), userPrincipal.getSubscribedChatId())
//                        .orElseThrow( () -> new NullPointerException("채팅방의 유저정보를 찾을 수 없습니다"));

        ChatMessageOutput recentMessage = redisCacheService.loadRecentChat(chatId.toString());
        Long recentMessageId;
        if(recentMessage == null){
            log.info("recent message not present for chatId : " + chatId);
        }
        else{
            recentMessageId = recentMessage.getChatRecordId();
            redisCacheService.addUnsubscribedUser(chatId.toString(), userPrincipal.getUserId().toString(), recentMessageId);
            log.info("adding recent message + " + recentMessageId + " to userId : " + userPrincipal.getUserId());
        }

//        chatJoinEntryRepository.save(chatJoinEntry);
        redisCacheService.removeSubscriptionFromChatId(chatId.toString(), sessionId);
        userPrincipal.setSubscribedChatId(-1L);
        redisCacheService.saveUserInfoToCache(sessionId,userPrincipal);
    }

}
