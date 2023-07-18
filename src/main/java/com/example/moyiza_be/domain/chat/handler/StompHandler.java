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
    private final StompUtils headerUtils;

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
//
//        if (StompCommand.SUBSCRIBE.equals(command) &&
//                MessageDestinationEnum.CHAT.equals(headerUtils.destinationChecker(headerAccessor.getDestination()))
//        ) {
//            return message;
//        }

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

    private void handleConnect(String sessionId, String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("The token is invalid.");
        }
        ChatUserPrincipal userInfo = jwtUtil.tokenToChatUserPrincipal(token);
        redisService.saveUserInfoToCache(sessionId, userInfo);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) &&
               MessageDestinationEnum.CHAT.equals(headerUtils.destinationChecker(headerAccessor.getDestination()))
        ) {
            String destination = headerAccessor.getDestination();
            String sessionId = headerAccessor.getSessionId();
            Long chatId = headerUtils.getChatIdFromDestination(destination);
            String subId = headerAccessor.getSubscriptionId();

            ChatUserPrincipal userPrincipal = redisService.getUserInfoFromCache(sessionId);
            String userId = userPrincipal.getUserId().toString();


            Long lastReadMessageId = redisService.getUserLastReadMessageId(chatId.toString(), userId);
            if (lastReadMessageId == null) {
                log.info("User " + userId + " has no lastReadMessage... continue without sending decrease message");
            } else {
                Message<byte[]> lastReadIdMessage = headerUtils.buildLastReadMessage(destination, lastReadMessageId, userId);
                channel.send(lastReadIdMessage);
                log.info("sending subscritionInfo of user : " + userPrincipal.getUserId() + " lastread message : " + lastReadMessageId);
            }

            redisService.saveChatSubscriptionDestination(subId, sessionId, chatId);
            redisService.saveSessionChatSubId(sessionId, subId);
            redisService.removeUnsubscribedUser(chatId.toString(), userPrincipal.getUserId().toString());
            redisService.addSubscriptionToChatId(chatId.toString(), userId);
        }

        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    private void handleChatUnsubscribe(ChatUserPrincipal userPrincipal, String subId, String sessionId) {
        Long chatId = redisService.removeAndGetChatSubcriptionDestination(subId, sessionId);
        if(chatId == null){
            log.debug("unsubscribe request for null chatId with subId : " + subId + " sessionId : " + sessionId);
            return;
        }

        String userId = userPrincipal.getUserId().toString();

        ChatMessageOutput recentMessage = redisService.loadRecentChat(chatId.toString());
        Long recentMessageId;

        if (recentMessage == null) {
            log.info("recent message not present for chatId : " + chatId);
        } else {
            recentMessageId = recentMessage.getChatRecordId();
            redisService.addUnsubscribedUser(chatId.toString(), userId);
            log.info("adding recent message " + recentMessageId + " to userId : " + userId);
        }

        redisService.removeSubIdFromSession(sessionId, subId);
        redisService.removeSubscriptionFromChatId(chatId.toString(), userId);
    }

}
