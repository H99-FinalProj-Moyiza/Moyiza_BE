package com.example.moyiza_be.common.redis;

import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCacheService {

//    private static final String SESSION_PREFIX = "session:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, ChatMessageOutput> redisRecentChatTemplate;
    private final RedisTemplate<String, String> redisStringStringTemplate;
    private final String RECENTCHAT_IDENTIFIER = ":recentChat";
    private final String CONNECTED_SESSIONS_IDENTIFIER = ":subscriptions";
    private final String LAST_MESSAGE_ZSET_IDENTIFIER = ":lastMessage";


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
        redisTemplate.delete(sessionId);
        log.info("removed userInfo cache for sessionId : "+ sessionId);
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

    public ChatMessageOutput loadRecentChat(String chatId){
        ListOperations<String, ChatMessageOutput> listOperations = redisRecentChatTemplate.opsForList();
        return listOperations.index(chatId + RECENTCHAT_IDENTIFIER, 0);
    }


    //그냥 숫자로 변경해도 될듯 ?
    public void addSubscriptionToChatId(String chatId, String sessionId){
        SetOperations<String, String> setOperations = redisStringStringTemplate.opsForSet();
        setOperations.add(chatId + CONNECTED_SESSIONS_IDENTIFIER, sessionId);
    }

    //그냥 숫자로 변경해도 될듯 ?
    public void removeSubscriptionFromChatId(String chatId, String sessionId){
        SetOperations<String, String> setOperations = redisStringStringTemplate.opsForSet();
        setOperations.remove(chatId + CONNECTED_SESSIONS_IDENTIFIER, sessionId);
    }

    public Long countSubscriptionToChatId(String chatId){
        SetOperations<String, String> setOperations = redisStringStringTemplate.opsForSet();
        return setOperations.size(chatId + CONNECTED_SESSIONS_IDENTIFIER);
    }

    public void addUnsubscribedUser(String chatId, String userId, Long lastReadMesssageId){
        ZSetOperations<String, String> zSetOperations = redisStringStringTemplate.opsForZSet();
        zSetOperations.add(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, userId, lastReadMesssageId);
    }

    public void removeUnsubscribedUser(String chatId, String userId){
        ZSetOperations<String, String> zSetOperations = redisStringStringTemplate.opsForZSet();
        zSetOperations.remove(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, userId);
    }

    public Long getReadCount(String chatId, Long nowMessageId){
        ZSetOperations<String, String> zSetOperations = redisStringStringTemplate.opsForZSet();
        return zSetOperations.count(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, nowMessageId, -1);
    }

    public Long getUserLastReadMessage(String chatId, String userId) {
        ZSetOperations<String, String> zSetOperations = redisStringStringTemplate.opsForZSet();
        Double score = zSetOperations.score(chatId + LAST_MESSAGE_ZSET_IDENTIFIER, userId);
        if(score == null){
            log.info("redis에서 유저 : " + userId + "의 마지막 읽은 메시지를 가져오는데 실패했습니다");
            return null;
        }
        return score.longValue();
    }

//
//////    public ChatUserPrincipal getUserInfoFromCache(String sessionId) {
//////        RedisConnection connection = redisConnectionFactory.getConnection();
//////        try {
//////            RedisAsyncCommands<String, ChatUserPrincipal> commands = connection.async();
//////            RedisFuture<ChatUserPrincipal> future = commands.get(sessionId);
////            return future.get();
////        } catch (InterruptedException | ExecutionException e) {
//            throw new IllegalArgumentException();
//        } finally {
//            connection.close();;
//        }
//        return null;
//    }

}
