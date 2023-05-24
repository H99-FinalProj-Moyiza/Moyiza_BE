package com.example.moyiza_be.user.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.user.dto.LoginRequestDto;
import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.dto.UpdateRequestDto;
import com.example.moyiza_be.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto){
        return userService.signup(requestDto);
    }
    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }
    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.logout(response, userDetails.getUser().getEmail());
    }
    //회원정보 수정
    @PutMapping(value = "/profile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateProfile(@RequestPart("imageFile") MultipartFile image,
                                           @RequestPart UpdateRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateProfile(image, requestDto, userDetails.getUser().getEmail());
    }

    //Refresh 토큰으로 Access 토큰 재발급
    @GetMapping("/reissue")
    public ResponseEntity<?> reissueToken(){
        return new ResponseEntity<>("AccessToken 재발행 성공", HttpStatus.OK);
    }
}
