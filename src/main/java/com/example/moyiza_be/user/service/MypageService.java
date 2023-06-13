package com.example.moyiza_be.user.service;

import com.example.moyiza_be.club.dto.ClubListOnMyPage;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.oneday.dto.OneDayListOnMyPage;
import com.example.moyiza_be.oneday.service.OneDayService;
import com.example.moyiza_be.user.dto.MyPageResponseDto;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final OneDayService oneDayService;
    private final ClubService clubService;

    public ResponseEntity<?> getMypage(User user) {
        ClubListOnMyPage clubListOnMyPage = clubService.getClubListOnMyPage(user.getId());
        OneDayListOnMyPage oneDayListOnMyPage = oneDayService.getOneDayListOnMyPage(user.getId());
        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(user, clubListOnMyPage, oneDayListOnMyPage);
        return ResponseEntity.ok(myPageResponseDto);
    }
}
