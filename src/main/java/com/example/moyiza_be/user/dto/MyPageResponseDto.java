package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.club.dto.ClubListOnMyPage;
import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.dto.OneDayDetailOnMyPage;
import com.example.moyiza_be.oneday.dto.OneDayListOnMyPage;
import com.example.moyiza_be.oneday.dto.OneDayListResponseDto;
import com.example.moyiza_be.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyPageResponseDto {
    private Long user_id;
    private String nickname;
    private String email;
    private String profileImage;
    private List<String> tags;
    private String content;
    private Integer clubsInOperationCount;
    private Integer clubsInParticipatingCount;
    private Integer oneDaysInOperationCount;
    private Integer oneDaysInParticipatingCount;
    private Page<ClubListResponse> clubsInOperationInfo;
    private Page<ClubListResponse> clubsInParticipatingInfo;
    private Page<OneDayListResponseDto> oneDaysInOperationInfo;
    private Page<OneDayListResponseDto> oneDaysInParticipatingInfo;


    public MyPageResponseDto(User profileUser, ClubListOnMyPage clubListOnMyPage, OneDayListOnMyPage oneDayListOnMyPage) {
        this.user_id = profileUser.getId();
        this.nickname = profileUser.getNickname();
        this.email = profileUser.getEmail();
        this.profileImage = profileUser.getProfileImage();
        this.tags = TagEnum.parseTag(profileUser.getTagString());
        this.content = profileUser.getContent();
        this.clubsInOperationCount = clubListOnMyPage.getClubsInOperationInfo().getNumberOfElements();
        this.clubsInParticipatingCount = clubListOnMyPage.getClubsInParticipatingInfo().getNumberOfElements();
        this.oneDaysInOperationCount = oneDayListOnMyPage.getOneDaysInOperationInfo().getNumberOfElements();
        this.oneDaysInParticipatingCount = oneDayListOnMyPage.getOneDaysInParticipatingInfo().getNumberOfElements();
        this.clubsInOperationInfo = clubListOnMyPage.getClubsInOperationInfo();
        this.clubsInParticipatingInfo = clubListOnMyPage.getClubsInParticipatingInfo();
        this.oneDaysInOperationInfo = oneDayListOnMyPage.getOneDaysInOperationInfo();
        this.oneDaysInParticipatingInfo = oneDayListOnMyPage.getOneDaysInParticipatingInfo();
    }
}
