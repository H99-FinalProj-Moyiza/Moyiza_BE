package com.example.moyiza_be.user.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.user.dto.*;
import com.example.moyiza_be.user.email.EmailRequestDto;
import com.example.moyiza_be.user.email.EmailService;
import com.example.moyiza_be.user.service.MypageService;
import com.example.moyiza_be.user.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final MypageService mypageService;

    //회원가입
    @PostMapping ("/signup")
    public ResponseEntity<?> signup(@RequestPart(value = "data") SignupRequestDto requestDto,
                                    @RequestPart(value = "imageFile")@Nullable MultipartFile image){
        return userService.signup(requestDto, image);
    }
    /*Temporary API for storing user information not received from the OAuth2 Provider
      To get all the information, we'll need to register and convert your business.
     */
    @PutMapping ("/signup/social")
    public ResponseEntity<?> updateSocialInfo(@RequestBody UpdateSocialInfoRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateSocialInfo(requestDto, userDetails.getUser());
    }
    @GetMapping ("/signup/social")
    public ResponseEntity<?> getSocialInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getSocialInfo(userDetails.getUser());
    }

    //Email Authentication - Send Email
    @PostMapping("/signup/confirmEmail")
    public ResponseEntity<?> confirmEmail(@RequestBody EmailRequestDto requestDto) throws Exception {
        return emailService.sendSimpleMessage(requestDto);
    }

    @PostMapping("/signup/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> codeMap) throws Exception {
        return emailService.verifyCode(codeMap.get("code"));
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }

    //Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.logout(request, response, userDetails.getUser().getEmail());
    }

    //Mypage
    @GetMapping("/mypage")
    public ResponseEntity<?> getMypage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMypage(userDetails.getUser());
    }


    //Modify Profile
    @PutMapping(value = "/profile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateProfile(@RequestPart(value = "imageFile") MultipartFile image,
                                           @RequestPart(value = "data") UpdateRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.updateProfile(image, requestDto, userDetails.getUser().getEmail());
    }

    //Reissue an Access token with a Refresh token
    @GetMapping("/reissue")
    public ResponseEntity<?> reissueToken(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken, HttpServletResponse response){
        return userService.reissueToken(refreshToken, response);
    }

    //Check for email duplicates
    @PostMapping("/check/email")
    public ResponseEntity<?> isDuplicatedEmail(@RequestBody CheckEmailRequestDto requestDto){
        return userService.isDuplicatedEmail(requestDto);
    }

    //Check for nickname duplicates
    @PostMapping("/check/nickname")
    public ResponseEntity<?> isDuplicatedNick(@RequestBody CheckNickRequestDto requestDto){
        return userService.isDuplicatedNick(requestDto);
    }
}
