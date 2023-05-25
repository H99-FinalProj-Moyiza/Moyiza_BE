package com.example.moyiza_be.mypage.controller;

import com.example.moyiza_be.club.dto.ClubResponseDto;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.event.dto.DataResponseDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.service.EventService;
import com.example.moyiza_be.mypage.Dto.PageResponseDto;
import com.example.moyiza_be.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("")
    public PageResponseDto getEvent(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getMyPage(userDetails.getUser());
    }
}
