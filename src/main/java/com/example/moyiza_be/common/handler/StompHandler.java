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
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

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
        if(StompCommand.CONNECT.equals(headerAccessor.getCommand())){
            log.info("Command : " + headerAccessor.getCommand());
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
            return message;
        }

        if(StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())){
            log.info("Command : " + headerAccessor.getCommand());
            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            unsubscribe(userPrincipal, sessionId);
        }

        if(StompCommand.DISCONNECT.equals(headerAccessor.getCommand())){
            log.info("Command : " + headerAccessor.getCommand());
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
        String chatId = userPrincipal.getSubscribedChatId().toString();
        String userId = userPrincipal.getUserId().toString();
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
            redisCacheService.addUnsubscribedUser(chatId, userId);
            log.info("adding recent message " + recentMessageId + " to userId : " + userId);
        }

//        chatJoinEntryRepository.save(chatJoinEntry);
        redisCacheService.removeSubscriptionFromChatId(chatId, userId);
        userPrincipal.setSubscribedChatId(-1L);
        redisCacheService.saveUserInfoToCache(sessionId,userPrincipal);


    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        
        if(StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())){
            String destination = headerAccessor.getDestination();
            String sessionId = headerAccessor.getSessionId();
            Long chatId = getChatIdFromDestination(destination);
            log.info("Command : " + headerAccessor.getCommand() + " for chatId : " + chatId);

            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            String userId = userPrincipal.getUserId().toString();

            userPrincipal.setSubscribedChatId(chatId);
            redisCacheService.saveUserInfoToCache(sessionId, userPrincipal);

            Long lastReadMessageId = redisCacheService.getUserLastReadMessageId(chatId.toString(), userId);
            if(lastReadMessageId == null){
                log.info("User " + userId + " has no lastReadMessage... continue without sending decrease message");
            }
            else{
                StompHeaderAccessor newHeader = StompHeaderAccessor.create(StompCommand.MESSAGE);
                newHeader.setDestination(destination);
                newHeader.setNativeHeader("lastReadMessage",String.valueOf(lastReadMessageId));
                newHeader.setNativeHeader("subscribedUserId", userId);
                Message<byte[]> newMessage = MessageBuilder.createMessage("new subscription".getBytes(StandardCharsets.UTF_8), newHeader.getMessageHeaders());
                channel.send(newMessage);
                log.info("sending subscritionInfo of user : " + userPrincipal.getUserId() + " lastread message : " + lastReadMessageId);
            }

            redisCacheService.removeUnsubscribedUser(chatId.toString(), userPrincipal.getUserId().toString());
            redisCacheService.addSubscriptionToChatId(chatId.toString(), userId);
        }

        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }
}
