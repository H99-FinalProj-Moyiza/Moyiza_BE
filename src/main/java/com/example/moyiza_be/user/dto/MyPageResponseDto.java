package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListOnMyPage;
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

//    private List<OnedayResponseDto> onedaysInOperationInfo;
//    private List<OnedayResponseDto> onedaysInParticipatingInfo;
//    private Integer onedaysInOperationCount;
//    private Integer onedaysInParticipatingCount;

    public MyPageResponseDto(User user, ClubListOnMyPage clubListOnMyPage) {
        this.user_id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.clubsInOperationCount = clubListOnMyPage.getClubsInOperationInfo().size();
        this.clubsInParticipatingCount = clubListOnMyPage.getClubsInParticipatingInfo().size();
        this.clubsInOperationInfo = clubListOnMyPage.getClubsInOperationInfo();
        this.clubsInParticipatingInfo = clubListOnMyPage.getClubsInParticipatingInfo();
//        this.onedaysInOperationInfo = onedaysInOperationInfo;
//        this.onedaysInParticipatingInfo = onedaysInParticipatingInfo;
    }
}
