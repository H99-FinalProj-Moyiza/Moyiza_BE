package com.example.moyiza_be.user.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.user.dto.*;
import com.example.moyiza_be.user.email.EmailRequestDto;
import com.example.moyiza_be.user.email.EmailService;
import com.example.moyiza_be.user.service.MypageService;
import com.example.moyiza_be.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final MypageService mypageService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto testRequestDto){
        return userService.signup(testRequestDto);
    }
    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadImg(@RequestPart(value = "imageFile") MultipartFile image){
        return userService.uploadImg(image);
    }

    /*OAuth2 Provider에서 받아오지 못하는 사용자 정보를 저장하기 위한 임시 api
      필요한 정보를 전부 받아오기 위해선 사업자 등록, 전환이 필요하다
     */
    @PutMapping ("/signup/social")
    public ResponseEntity<?> updateSocialInfo(@RequestBody UpdateSocialInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateSocialInfo(requestDto, userDetails.getUser());
    }
    @GetMapping ("/signup/social")
    public ResponseEntity<?> getSocialInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getSocialInfo(userDetails.getUser());
    }

    //이메일 인증 - 이메일 전송
    @PostMapping("/signup/confirmEmail")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailRequestDto requestDto) throws Exception {
        return emailService.sendSimpleMessage(requestDto);
    }

    @PostMapping("/signup/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> codeMap) throws Exception {
        return emailService.verifyCode(codeMap.get("code"));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.logout(request, response, userDetails.getUser().getEmail());
    }

    //마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<?> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMypage(userDetails.getUser());
    }

    //회원정보 수정 - 관심사 추가 tagList 조회
    @GetMapping("/profile/tags")
    public ResponseEntity<?> tagsOfCategory(@RequestParam String category){
        return userService.tagsOfCategory(category);
    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfileTest(@RequestBody UpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateProfile(requestDto, userDetails.getUser().getEmail());
    }

    //Refresh 토큰으로 Access 토큰 재발급
    @GetMapping("/reissue")
    public ResponseEntity<?> reissueToken(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken, HttpServletResponse response){
        return userService.reissueToken(refreshToken, response);
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

    //이메일 찾기 - 문자 전송
    @PostMapping("/find/email")
    public ResponseEntity<?> findUserEmail(@RequestBody FindEmailRequestDto requestDto){
        return userService.sendSmsToFindEmail(requestDto);
    }
    //이메일 찾기 - 코드 검증
    @PostMapping("/find/email/verifyCode")
    public ResponseEntity<?> findUserEmailVerifyCode(@RequestBody Map<String, String> codeMap) throws Exception {
        return userService.verifyCodeToFindEmail(codeMap.get("code"));
    }

    //profile test
//    @PutMapping(value = "/profile",
//            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<?> updateProfile(@RequestPart(value = "imageFile") MultipartFile image,
//                                           @RequestPart(value = "data") UpdateRequestDto requestDto,
//                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return userService.updateProfile(image, requestDto, userDetails.getUser().getEmail());
//    }
}
