package com.example.moyiza_be.common.redis;

import com.example.moyiza_be.chat.dto.ChatUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
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

        hashOperations.putAll(sessionId, userInfoMap);
    }

    //userInfo 조회
    public ChatUserPrincipal getUserInfoFromCache(String sessionId) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        Map<Object, Object> userInfoMap = hashOperations.entries(sessionId);

        Long userId = Long.valueOf(userInfoMap.get("userId").toString());
        String nickname = userInfoMap.get("nickname").toString();
        String profileUrl = userInfoMap.get("profileUrl").toString();
        ChatUserPrincipal userInfo = new ChatUserPrincipal(userId, nickname, profileUrl);
        return userInfo;
    }


//    public ChatUserPrincipal getUserInfoFromCache(String sessionId) {
//        RedisConnection connection = redisConnectionFactory.getConnection();
//        try {
//            RedisAsyncCommands<String, ChatUserPrincipal> commands = connection.async();
//            RedisFuture<ChatUserPrincipal> future = commands.get(sessionId);
//            return future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new IllegalArgumentException();
//        } finally {
//            connection.close();;
//        }
//        return null;
//    }

}
