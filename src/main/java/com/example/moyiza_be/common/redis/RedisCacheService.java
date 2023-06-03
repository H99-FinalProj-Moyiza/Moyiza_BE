package com.example.moyiza_be.common.redis;

import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCacheService {

//    private static final String SESSION_PREFIX = "session:";
    private final RedisTemplate<String, Object> redisTemplate;

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
