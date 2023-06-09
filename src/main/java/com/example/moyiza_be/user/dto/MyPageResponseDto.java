package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListOnMyPage;
import com.example.moyiza_be.oneday.dto.OneDayDetailResponseDto;
import com.example.moyiza_be.oneday.dto.OneDayListOnMyPage;
import com.example.moyiza_be.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    //유저 정보, 운영중 클럽, 참여중 클럽, 운영중 원데이, 참여중 원데이
    private Long user_id;
    private String nickname;
    private String email;
    private String profileImage;
    private Integer clubsInOperationCount;
    private Integer clubsInParticipatingCount;
    private List<ClubDetailResponse> clubsInOperationInfo;
    private List<ClubDetailResponse> clubsInParticipatingInfo;
    private Integer oneDaysInOperationCount;
    private Integer oneDaysInParticipatingCount;
    private List<OneDayDetailResponseDto> oneDaysInOperationInfo;
    private List<OneDayDetailResponseDto> oneDaysInParticipatingInfo;


    public MyPageResponseDto(User user, ClubListOnMyPage clubListOnMyPage, OneDayListOnMyPage oneDayListOnMyPage) {
        this.user_id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.clubsInOperationCount = clubListOnMyPage.getClubsInOperationInfo().size();
        this.clubsInParticipatingCount = clubListOnMyPage.getClubsInParticipatingInfo().size();
        this.clubsInOperationInfo = clubListOnMyPage.getClubsInOperationInfo();
        this.clubsInParticipatingInfo = clubListOnMyPage.getClubsInParticipatingInfo();
        this.oneDaysInOperationCount = oneDayListOnMyPage.getOneDaysInOperationInfo().size();
        this.oneDaysInParticipatingCount = oneDayListOnMyPage.getOneDaysInParticipatingInfo().size();
        this.oneDaysInOperationInfo = oneDayListOnMyPage.getOneDaysInOperationInfo();
        this.oneDaysInParticipatingInfo = oneDayListOnMyPage.getOneDaysInParticipatingInfo();
    }
}
