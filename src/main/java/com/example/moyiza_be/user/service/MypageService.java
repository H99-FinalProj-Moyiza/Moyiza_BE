package com.example.moyiza_be.user.service;

import com.example.moyiza_be.club.dto.ClubListOnMyPage;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.oneday.dto.OneDayListOnMyPage;
import com.example.moyiza_be.oneday.service.OneDayService;
import com.example.moyiza_be.user.dto.MyPageResponseDto;
import com.example.moyiza_be.user.dto.UserInfoOnMyPage;
import com.example.moyiza_be.user.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MypageService {
    private final OneDayService oneDayService;
    private final ClubService clubService;

    //마이페이지
    public ResponseEntity<?> getMypage(User user) {
        ClubListOnMyPage clubListOnMyPage = clubService.getClubListOnMyPage(user.getId());
        OneDayListOnMyPage oneDayListOnMyPage = oneDayService.getOneDayListOnMyPage(user.getId());
        Integer clubsInOperationCount = clubListOnMyPage.getClubsInOperationInfo().size();
        Integer clubsInParticipatingCount = clubListOnMyPage.getClubsInParticipatingInfo().size();
        Integer oneDaysInOperationCount = oneDayListOnMyPage.getOneDaysInOperationInfo().size();
        Integer oneDaysInParticipatingCount = oneDayListOnMyPage.getOneDaysInParticipatingInfo().size();
        UserInfoOnMyPage userInfoOnMyPage = new UserInfoOnMyPage(user, clubsInOperationCount, clubsInParticipatingCount,
                oneDaysInOperationCount, oneDaysInParticipatingCount);
        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(userInfoOnMyPage, clubListOnMyPage, oneDayListOnMyPage);
        return ResponseEntity.ok(myPageResponseDto);
    }
}
