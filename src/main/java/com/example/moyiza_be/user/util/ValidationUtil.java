package com.example.moyiza_be.user.util;

import com.example.moyiza_be.common.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

    private final RedisUtil redisUtil;

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
