package com.example.moyiza_be.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClubListOnMyPage {
    private List<ClubDetailResponse> clubsInOperationInfo;
    private List<ClubDetailResponse> clubsInParticipatingInfo;

    public ClubListOnMyPage(List<ClubDetailResponse> clubsInOperationInfo,
                            List<ClubDetailResponse> clubsInParticipatingInfo) {
        this.clubsInOperationInfo = clubsInOperationInfo;
        this.clubsInParticipatingInfo = clubsInParticipatingInfo;
    }
}
