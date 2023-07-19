package com.example.moyiza_be.common.redis;

import com.example.moyiza_be.domain.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.domain.chat.dto.ChatUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, ChatMessageOutput> redisRecentChatTemplate;
    private final RedisTemplate<String, ChatUserPrincipal> redisPrincipalTemplate;
    private final RedisTemplate<String, String> redisStringTemplate;
    private final RedisTemplate<String, Long> redisLongtemplate;
    private final String RECENTCHAT_IDENTIFIER = ":recentChat";
    private final String CONNECTED_USERS_IDENTIFIER = ":subscriptions";
    private final String LAST_MESSAGE_ZSET_IDENTIFIER = ":lastMessage";
    private final String CHAT_DESTINATION = ":destination";
    private final String SESSION_SUBIDLIST = ":subIdList";
    private final String SUB_INFO_POSTFIX = "_subs";
    private final String USERINFO_KEY = "user";


    //세션id로 userInfo 저장
//    public void saveUserInfoToCache(String sessionId, ChatUserPrincipal userInfo) {
//        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
//        HashMap<String, Object> userInfoMap = new HashMap<>();
//        userInfoMap.put("userId", userInfo.getUserId());
//        userInfoMap.put("nickname", userInfo.getUserNickname());
//        userInfoMap.put("profileUrl", userInfo.getProfileUrl());
//        hashOperations.putAll(sessionId, userInfoMap);
////        log.info("updating/saving userInfo : " + userInfo.getUserId() + " for sessionId : " + sessionId);
//    }
    public void saveUserInfoToCache(String sessionId, ChatUserPrincipal principal) {
        HashOperations<String, String, ChatUserPrincipal> hashOperations = redisPrincipalTemplate.opsForHash();
        hashOperations.put(USERINFO_KEY, sessionId, principal);
    }
    public ChatUserPrincipal getUserInfoFromCache(String sessionId) {
        HashOperations<String, String, ChatUserPrincipal> hashOperations = redisPrincipalTemplate.opsForHash();
        return hashOperations.get(USERINFO_KEY, sessionId);
    }

    public void deleteUserInfoFromCache(String sessionId) {
        redisTemplate.delete(sessionId);
        log.info("removed userInfo cache for sessionId : " + sessionId);
    }

    public Set<String> getSessionSubIdSet(String sessionId) {
        SetOperations<String, String> setOperations = redisStringTemplate.opsForSet();
        return setOperations.members(sessionId + SESSION_SUBIDLIST);
    }

    public void addRecentChatToList(String chatId, ChatMessageOutput messageOutput) {
        ListOperations<String, ChatMessageOutput> listOperations = redisRecentChatTemplate.opsForList();
        listOperations.leftPush(chatId + RECENTCHAT_IDENTIFIER, messageOutput);
        listOperations.trim(chatId, 0, 50);
    }

    public Page<ChatMessageOutput> loadRecentChatList(String chatId, Long chatMemberCount, Pageable pageable) {
        ListOperations<String, ChatMessageOutput> listOperations = redisRecentChatTemplate.opsForList();
        List<ChatMessageOutput> recentChatList =
                listOperations.range(chatId + RECENTCHAT_IDENTIFIER, 0, 50);

        if (recentChatList == null) {
            return new PageImpl<>(new LinkedList<>(), pageable, 0L);
        } else {
            for (ChatMessageOutput chatMessage : recentChatList) {
                chatMessage.setUnreadCount(chatMemberCount - getTotalReadCount(chatId, chatMessage.getChatRecordId()));
            }
        }
        return new PageImpl<>(recentChatList, pageable, -1L);
    }

    public ChatMessageOutput loadRecentChat(String chatId) {
        ListOperations<String, ChatMessageOutput> listOperations = redisRecentChatTemplate.opsForList();
        return listOperations.index(chatId + RECENTCHAT_IDENTIFIER, 0);
    }

    public void addSubscriptionToChatId(String chatId, String userId) {
        SetOperations<String, String> setOperations = redisStringTemplate.opsForSet();
        setOperations.add(chatId + CONNECTED_USERS_IDENTIFIER, userId);
    }

    public void removeSubscriptionFromChatId(String chatId, String userId) {
        SetOperations<String, String> setOperations = redisStringTemplate.opsForSet();
        setOperations.remove(chatId + CONNECTED_USERS_IDENTIFIER, userId);
    }

    public Long countSubscriptionToChatId(String chatId) {
        SetOperations<String, String> setOperations = redisStringTemplate.opsForSet();
        return setOperations.size(chatId + CONNECTED_USERS_IDENTIFIER);
    }

    public void addUnsubscribedUser(String chatId, String userId) {
        ZSetOperations<String, String> zSetOperations = redisStringTemplate.opsForZSet();
        ChatMessageOutput recentMessage = loadRecentChat(chatId);
        if (recentMessage == null) {
            log.info("recent message not present for chatId : " + chatId);
        } else {
            zSetOperations.add(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, userId, recentMessage.getChatRecordId());
        }
    }

    public void removeUnsubscribedUser(String chatId, String userId) {
        ZSetOperations<String, String> zSetOperations = redisStringTemplate.opsForZSet();
        zSetOperations.remove(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, userId);
    }

    public Long getInactiveReadCount(String chatId, Long nowMessageId) {
        ZSetOperations<String, String> zSetOperations = redisStringTemplate.opsForZSet();
        try {
            return zSetOperations.count(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, nowMessageId, Double.POSITIVE_INFINITY);
        } catch (Exception e) {
            log.info("error counting inactive readCount : " + e.getMessage());
            throw e;
        }
    }

    public Long getTotalReadCount(String chatId, Long nowMessageId) {
        return getInactiveReadCount(chatId, nowMessageId) + countSubscriptionToChatId(chatId);
    }

    public Long getUserLastReadMessageId(String chatId, String userId) {
        ZSetOperations<String, String> zSetOperations = redisStringTemplate.opsForZSet();
        Double score = zSetOperations.score(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, userId);
        if (score == null) {
            return null;
        }
        return score.longValue();
    }

    public String getChatIdFromSubId(String subId, String sessionId) {
        ValueOperations<String, String> valueOperations = redisStringTemplate.opsForValue();
        return valueOperations.get(subId + sessionId + CHAT_DESTINATION);
    }


    //pipeline 만들어주면 좋을듯
    public void unsubscribeChat(String sessionId, String subId, String userId){
        HashOperations<String, String, String> hashOperations= redisStringTemplate.opsForHash();
        String chatId = hashOperations.get(sessionId, subId);
        if (chatId == null){
            log.debug("unsubscribe request for null chatId with subId : " + subId + " sessionId : " + sessionId);
            return;
        }
        addUnsubscribedUser(chatId, userId);
        deleteSessionSubInfo(sessionId, subId);
        removeSubscriptionFromChatId(chatId, userId);

    }


    //for SUB_INFO (sessionId -> subId -> chatId)
    public void addSessionSubInfo(String sessionId, String subId, Long chatId, String userId){
        HashOperations<String, String, String> hashOperations= redisStringTemplate.opsForHash();
        hashOperations.put(sessionId + SUB_INFO_POSTFIX, subId, chatId.toString());
        addSubscriptionToChatId(chatId.toString(), userId);
    }

    public void deleteSessionSubInfo(String sessionId, String subId){
        HashOperations<String, String, String> hashOperations= redisStringTemplate.opsForHash();
        hashOperations.delete(sessionId + SUB_INFO_POSTFIX, subId);
    }


}
