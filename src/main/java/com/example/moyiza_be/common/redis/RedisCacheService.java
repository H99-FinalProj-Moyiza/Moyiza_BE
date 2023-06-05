package com.example.moyiza_be.common.redis;

import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import com.example.moyiza_be.chat.entity.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCacheService {

//    private static final String SESSION_PREFIX = "session:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, ChatMessageOutput> redisRecentChatTemplate;
    private final RedisTemplate<String, String> redisStringListTemplate;
    private final String RECENTCHAT_IDENTIFIER = ":recentChat";
    private final String CONNECTED_SESSIONS_IDENTIFIER = ":subscriptions";


    //세션id 생성
//    public String generateSessionId() {
//        return SESSION_PREFIX + UUID.randomUUID().toString();
//    }

    //세션id로 userInfo 저장
    public void saveUserInfoToCache(String sessionId, ChatUserPrincipal userInfo) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        HashMap<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("userId", userInfo.getUserId());
        userInfoMap.put("nickname", userInfo.getUserNickname());
        userInfoMap.put("profileUrl", userInfo.getProfileUrl());
        userInfoMap.put("subscribedChatId", userInfo.getSubscribedChatId());
        hashOperations.putAll(sessionId, userInfoMap);
        log.info("saving user : " + userInfo.getUserId() + " for sessionId : " + sessionId);
    }

    //userInfo 조회
    public ChatUserPrincipal getUserInfoFromCache(String sessionId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<Object, Object> userInfoMap = hashOperations.entries(sessionId);

        Long userId = Long.valueOf(userInfoMap.get("userId").toString());
        String nickname = userInfoMap.get("nickname").toString();
        String profileUrl = userInfoMap.get("profileUrl").toString();
        Long subscribedChatId = Long.valueOf(userInfoMap.get("subscribedChatId").toString());
        return new ChatUserPrincipal(userId, nickname, profileUrl, subscribedChatId);
    }

    public void deleteUserInfoFromCache(String sessionId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        log.info("removing userInfo cache for sessionId : "+ sessionId);
        hashOperations.delete(sessionId);
    }

    public void addRecentChatToList(String chatId, ChatMessageOutput messageOutput){
        ListOperations<String, ChatMessageOutput> listOperations = redisRecentChatTemplate.opsForList();
        listOperations.leftPush(chatId + RECENTCHAT_IDENTIFIER,messageOutput);
        listOperations.trim(chatId, 0, 50);
    }

    public Page<ChatMessageOutput> loadRecentChatList(String chatId, Pageable pageable){
        ListOperations<String, ChatMessageOutput> listOperations = redisRecentChatTemplate.opsForList();
        List<ChatMessageOutput> recentChatList = listOperations.range(chatId + RECENTCHAT_IDENTIFIER, 0, 50);
        if (recentChatList == null){
            return new PageImpl<>(new LinkedList<>(), pageable, 0L);
        }

        return new PageImpl<>(recentChatList, pageable, -1L);
    }

    public void addSubscriptionToChatId(String chatId, String sessionId){
        SetOperations<String, String> setOperations = redisStringListTemplate.opsForSet();
        setOperations.add(chatId + CONNECTED_SESSIONS_IDENTIFIER);
    }

    public void removeSubscriptionFromChatId(String chatId, String sessionId){
        SetOperations<String, String> setOperations = redisStringListTemplate.opsForSet();
        setOperations.remove(chatId + CONNECTED_SESSIONS_IDENTIFIER, sessionId);
    }

    public void countSubscriptionToChatId(String chatId){
        SetOperations<String, String> setOperations = redisStringListTemplate.opsForSet();
        setOperations.size(chatId + CONNECTED_SESSIONS_IDENTIFIER);
    }

//
////    public ChatUserPrincipal getUserInfoFromCache(String sessionId) {
////        RedisConnection connection = redisConnectionFactory.getConnection();
////        try {
////            RedisAsyncCommands<String, ChatUserPrincipal> commands = connection.async();
////            RedisFuture<ChatUserPrincipal> future = commands.get(sessionId);
////            return future.get();
////        } catch (InterruptedException | ExecutionException e) {
//            throw new IllegalArgumentException();
//        } finally {
//            connection.close();;
//        }
//        return null;
//    }

}
