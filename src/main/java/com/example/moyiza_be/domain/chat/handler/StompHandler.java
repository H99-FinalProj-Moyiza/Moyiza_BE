package com.example.moyiza_be.domain.chat.handler;


import com.example.moyiza_be.common.enums.MessageDestinationEnum;
import com.example.moyiza_be.common.redis.RedisService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.domain.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.domain.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.domain.chat.utils.StompUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
@Slf4j

public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final StompUtils stompUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();
        StompCommand command = headerAccessor.getCommand();
        log.info("Command : " + command);

        if (StompCommand.CONNECT.equals(command)) {
            String bearerToken = headerAccessor.getFirstNativeHeader("ACCESS_TOKEN");
            String token = jwtUtil.removePrefix(bearerToken);
            handleConnect(sessionId, token);
            return message;
        }

        if (StompCommand.UNSUBSCRIBE.equals(command)
        ) {
            String subId = headerAccessor.getSubscriptionId();
            if(redisService.getChatIdFromSubId(subId, sessionId) != null){
                ChatUserPrincipal userPrincipal = redisService.getUserInfoFromCache(sessionId);
                handleChatUnsubscribe(userPrincipal, subId, sessionId);
            }
        }

        if (StompCommand.DISCONNECT.equals(command)
        ) {
            ChatUserPrincipal userPrincipal = redisService.getUserInfoFromCache(sessionId);
            Set<String> chatSubIdSet = redisService.getSessionSubIdSet(sessionId);
            if (chatSubIdSet == null || chatSubIdSet.size() == 0) {
                log.info("User is not subscribed to any chat .. continue DISCONNECT");
            } else {
                for(String subId:chatSubIdSet){
                    handleChatUnsubscribe(userPrincipal, subId, sessionId);
                }
            }
            redisService.deleteUserInfoFromCache(sessionId);
        }

        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) &&
               MessageDestinationEnum.CHAT.equals(stompUtils.destinationChecker(headerAccessor.getDestination()))
        ) {
            String destination = headerAccessor.getDestination();
            Long chatId = stompUtils.getChatIdFromDestination(destination);

            String sessionId = headerAccessor.getSessionId();
            String subId = headerAccessor.getSubscriptionId();

            ChatUserPrincipal userPrincipal = redisService.getUserInfoFromCache(sessionId);
            String userId = userPrincipal.getUserId().toString();

            Long lastReadMessageId = redisService.getUserLastReadMessageId(chatId.toString(), userId);

            if (lastReadMessageId == null) {
                log.info("User " + userId + " has no lastReadMessage... continue without sending decrease message");
            } else {
                stompUtils.sendLastReadMessage(destination, lastReadMessageId, userId, channel);
            }

            redisService.addSessionSubInfo(sessionId, subId, chatId, userId);
            redisService.removeUnsubscribedUser(chatId.toString(), userPrincipal.getUserId().toString());
            redisService.addSubscriptionToChatId(chatId.toString(), userId);

        }

        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    private void handleConnect(String sessionId, String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("The token is invalid.");
        }
        ChatUserPrincipal userInfo = jwtUtil.tokenToChatUserPrincipal(token);
        redisService.saveUserInfoToCache(sessionId, userInfo);
    }

    private void handleSubscribe(String sessionId, String subId, String chatId){

    }

    private void handleChatUnsubscribe(ChatUserPrincipal userPrincipal, String subId, String sessionId) {
        String userId = userPrincipal.getUserId().toString();
        redisService.unsubscribeChat(subId, sessionId, userId);
    }

}
