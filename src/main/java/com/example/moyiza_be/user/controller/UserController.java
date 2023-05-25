package com.example.moyiza_be.user.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.user.dto.*;
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
    @RequestMapping (value = "/signup", method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> signup(@RequestPart(value = "data") SignupRequestDto requestDto,
                                    @RequestPart(value = "imageFile") MultipartFile image){
        return userService.signup(requestDto, image);
    }

    //회원가입 테스트
    @PostMapping("/test/upload")
    public ResponseEntity<?> uploadTest(@RequestPart(value = "imageFile") MultipartFile image){
        return userService.uploadTest(image);
    }
    @PostMapping("/test/signup")
    public ResponseEntity<?> signupTest(@RequestBody TestSignupRequestDto testRequestDto){
        return userService.signupTest(testRequestDto);
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

    //이메일 중복 확인
    @PostMapping("/check/email")
    public ResponseEntity<?> isDuplicatedEmail(@RequestBody CheckEmailRequestDto requestDto){
        return userService.isDuplicatedEmail(requestDto);
    }

    //닉네임 중복 확인
    @PostMapping("/check/nickname")
    public ResponseEntity<?> isDuplicatedNick(@RequestBody CheckNickRequestDto requestDto){
        return userService.isDuplicatedNick(requestDto);
    }
}
