package com.example.moyiza_be.domain.oneday.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class OneDayListOnMyPage {
    private Page<OneDayListResponseDto> oneDaysInOperationInfo;
    private Page<OneDayListResponseDto> oneDaysInParticipatingInfo;

    public OneDayListOnMyPage(Page<OneDayListResponseDto> oneDaysInOperationInfo,
                              Page<OneDayListResponseDto> oneDaysInParticipatingInfo) {
        this.oneDaysInOperationInfo = oneDaysInOperationInfo;
        this.oneDaysInParticipatingInfo = oneDaysInParticipatingInfo;
    }
}
