package com.example.moyiza_be.user.util;

import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.common.redis.RedisUtil;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->
                new NoSuchElementException("사용자가 존재하지 않습니다."));
    }
    public User findUserBySocialInfo(SocialType socialType, String socialLoginId){
        return userRepository.findBySocialTypeAndSocialLoginId(socialType, socialLoginId).orElseThrow(()->
                new NoSuchElementException("사용자가 존재하지 않습니다."));
    }
    public void checkDuplicatedEmail(String email){
        Optional<User> findUserByEmail = userRepository.findByEmail(email);
        if (findUserByEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일 사용");
        }
    }
    public void checkDuplicatedNick(String nickname){
        Optional<User> findUserByNickname = userRepository.findByNickname(nickname);
        if (findUserByNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 사용");
        }
    }

    public void verifyCode(String code) {
        if (redisUtil.getData(code) == null){
            throw new IllegalArgumentException("옳지 않은 인증번호 입니다.");
        }
        redisUtil.deleteData(code);
    }

    // 인증코드 만들기
    public String createCode() {
        StringBuffer code = new StringBuffer();
        Random randomNum = new Random();

        for (int i = 0; i < 6; i++) {
            code.append((randomNum.nextInt(10)));
        }
        return code.toString();
    }
}
