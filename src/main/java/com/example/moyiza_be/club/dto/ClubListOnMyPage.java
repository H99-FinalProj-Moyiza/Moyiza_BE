package com.example.moyiza_be.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClubListOnMyPage {
    private Page<ClubListResponse> clubsInOperationInfo;
    private Page<ClubListResponse> clubsInParticipatingInfo;

    public ClubListOnMyPage(Page<ClubListResponse> clubsInOperationInfo,
                            Page<ClubListResponse> clubsInParticipatingInfo) {
        this.clubsInOperationInfo = clubsInOperationInfo;
        this.clubsInParticipatingInfo = clubsInParticipatingInfo;
    }
}
