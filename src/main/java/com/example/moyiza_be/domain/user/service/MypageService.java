package com.example.moyiza_be.domain.user.service;

import com.example.moyiza_be.domain.club.dto.ClubListOnMyPage;
import com.example.moyiza_be.domain.club.dto.ClubListResponse;
import com.example.moyiza_be.domain.club.service.ClubService;
import com.example.moyiza_be.domain.oneday.dto.OneDayListOnMyPage;
import com.example.moyiza_be.domain.oneday.dto.OneDayListResponseDto;
import com.example.moyiza_be.domain.oneday.service.OneDayService;
import com.example.moyiza_be.domain.user.dto.LikeResponseDto;
import com.example.moyiza_be.domain.user.dto.MyPageResponseDto;
import com.example.moyiza_be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserService userService;
    private final OneDayService oneDayService;
    private final ClubService clubService;

    public ResponseEntity<?> getMypage(Pageable pageable, User user, Long profileId) {
        User profileUser = userService.loadUserById(profileId);
        ClubListOnMyPage clubListOnMyPage = clubService.getClubListOnMyPage(pageable, user, profileId);
        OneDayListOnMyPage oneDayListOnMyPage = oneDayService.getOneDayListOnMyPage(pageable ,user, profileId);
        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(profileUser, clubListOnMyPage, oneDayListOnMyPage);
        return ResponseEntity.ok(myPageResponseDto);
    }

    public ResponseEntity<?> getLikeList(Pageable pageable, User user, Long profileId) {
        Page<ClubListResponse> likeClubList = clubService.getLikeClubListOnMypage(pageable, user, profileId);
        Page<OneDayListResponseDto> likeOneDayList = oneDayService.getLikeOneDayListOnMypage(pageable, user, profileId);
        LikeResponseDto likeResponseDto = new LikeResponseDto(likeClubList, likeOneDayList);
        return ResponseEntity.ok(likeResponseDto);
    }
}
