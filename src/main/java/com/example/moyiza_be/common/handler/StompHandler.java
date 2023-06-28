package com.example.moyiza_be.common.handler;


import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.common.enums.MessageDestinationEnum;
import com.example.moyiza_be.common.redis.RedisCacheService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
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
import java.util.Set;

@RequiredArgsConstructor
@Component
@Slf4j

public class StompHandler implements ChannelInterceptor {
    private final JwtUtil jwtUtil;
    private final RedisCacheService redisCacheService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();
//        log.info("Command : " + headerAccessor.getCommand());

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            String bearerToken = headerAccessor.getFirstNativeHeader("ACCESS_TOKEN");
            String token = jwtUtil.removePrefix(bearerToken);
            if (!jwtUtil.validateToken(token)) {
                throw new IllegalArgumentException("The token is invalid.");
            }
            ChatUserPrincipal userInfo = jwtUtil.tokenToChatUserPrincipal(token);
            redisCacheService.saveUserInfoToCache(sessionId, userInfo);
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) &&
                MessageDestinationEnum.CHAT.equals(destinationChecker(headerAccessor.getDestination()))
        ) {
            return message;
        }

        if (StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())
//                MessageDestinationEnum.CHAT.equals(destinationChecker(headerAccessor.getDestination()))
        ) {
            String subId = headerAccessor.getSubscriptionId();
            if(redisCacheService.getChatIdFromSubId(subId, sessionId) != null){
                ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
                handleChatUnsubscribe(userPrincipal, subId, sessionId);
            }
        }

        if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())
//                MessageDestinationEnum.CHAT.equals(destinationChecker(headerAccessor.getDestination()))
        ) {
            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            Set<String> chatSubIdSet = redisCacheService.getSessionSubIdSet(sessionId);
            if (chatSubIdSet == null || chatSubIdSet.size() == 0) {
                log.info("User is not subscribed to any chat .. continue DISCONNECT");
            } else {
                for(String subId:chatSubIdSet){
                    handleChatUnsubscribe(userPrincipal, subId, sessionId);
                }
            }
            redisCacheService.deleteUserInfoFromCache(sessionId);
        }

        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) &&
               MessageDestinationEnum.CHAT.equals(destinationChecker(headerAccessor.getDestination()))
        ) {
            String destination = headerAccessor.getDestination();
            String sessionId = headerAccessor.getSessionId();
            Long chatId = getChatIdFromDestination(destination);
            String subId = headerAccessor.getSubscriptionId();
//            log.info("activate SUBSCRIBE AfterCompletion Method for chatId : " + chatId);

            ChatUserPrincipal userPrincipal = redisCacheService.getUserInfoFromCache(sessionId);
            String userId = userPrincipal.getUserId().toString();


//            disable for JMeter test
            Long lastReadMessageId = redisCacheService.getUserLastReadMessageId(chatId.toString(), userId);
            if (lastReadMessageId == null) {
                log.info("User " + userId + " has no lastReadMessage... continue without sending decrease message");
            } else {
                Message<byte[]> lastReadIdMessage = buildLastReadMessage(destination, lastReadMessageId, userId);
                channel.send(lastReadIdMessage);
                log.info("sending subscritionInfo of user : " + userPrincipal.getUserId() + " lastread message : " + lastReadMessageId);
            }

            redisCacheService.saveChatSubscriptionDestination(subId, sessionId, chatId);
            redisCacheService.saveSessionChatSubId(sessionId, subId);
            redisCacheService.removeUnsubscribedUser(chatId.toString(), userPrincipal.getUserId().toString());
            redisCacheService.addSubscriptionToChatId(chatId.toString(), userId);
        }

        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }


    ////////////////////////////////////////

    private Long getChatIdFromDestination(String destination) {
        if (destination == null) {
            throw new NullPointerException("subscribe destination not defined");
        }
        return Long.valueOf(destination.replaceAll("\\D", ""));
    }

    private MessageDestinationEnum destinationChecker(String destination) {
        if(destination == null){
            return null;
        }
        String type = destination.split("/")[1];
        if (type.equals("chat")) {
            return MessageDestinationEnum.CHAT;
        }
        if (type.equals("chatalarm")) {
            return MessageDestinationEnum.CHATALARM;
        }
        else{
            throw new IllegalArgumentException("unknown destination type");
        }
    }

    private void handleChatUnsubscribe(ChatUserPrincipal userPrincipal, String subId, String sessionId) {
        Long chatId = redisCacheService.removeAndGetChatSubcriptionDestination(subId, sessionId);
        if(chatId == null){
            log.debug("unsubscribe request for null chatId with subId : " + subId + " sessionId : " + sessionId);
            return;
        }
//        log.info(userPrincipal.getUserId() + " unsubscribing chatroom : " + chatId);
        String userId = userPrincipal.getUserId().toString();

        ChatMessageOutput recentMessage = redisCacheService.loadRecentChat(chatId.toString());
        Long recentMessageId;

//        disable for Jmeter Test
        if (recentMessage == null) {
            log.info("recent message not present for chatId : " + chatId);
        } else {
            recentMessageId = recentMessage.getChatRecordId();
            redisCacheService.addUnsubscribedUser(chatId.toString(), userId);
            log.info("adding recent message " + recentMessageId + " to userId : " + userId);
        }

        redisCacheService.removeSubIdFromSession(sessionId, subId);
        redisCacheService.removeSubscriptionFromChatId(chatId.toString(), userId);
    }

    //When a user SUBSCRIBES, this message tells the client the ID of the last message read by that user.
    private Message<byte[]> buildLastReadMessage(String destination, Long lastReadMessageId, String userId) {
        StompHeaderAccessor newHeader = StompHeaderAccessor.create(StompCommand.MESSAGE);
        newHeader.setDestination(destination);
        newHeader.setNativeHeader("lastReadMessage", String.valueOf(lastReadMessageId));
        newHeader.setNativeHeader("subscribedUserId", userId);
        return MessageBuilder.createMessage("new subscription".getBytes(StandardCharsets.UTF_8), newHeader.getMessageHeaders());
    }

}
