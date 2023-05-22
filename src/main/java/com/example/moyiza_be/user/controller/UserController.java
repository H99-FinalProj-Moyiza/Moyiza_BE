package com.example.moyiza_be.user.controller;

import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //회원가입
    @RequestMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }
    //로그인

    //로그아웃

    //회원정보 수정

    //Refresh 토큰으로 Access 토큰 재발급
}
