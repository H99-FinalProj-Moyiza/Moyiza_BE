package com.example.moyiza_be.oneday.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OneDayListOnMyPage {
    private List<OneDayDetailResponseDto> oneDaysInOperationInfo;
    private List<OneDayDetailResponseDto> oneDaysInParticipatingInfo;

    public OneDayListOnMyPage(List<OneDayDetailResponseDto> oneDaysInOperationInfo,
                              List<OneDayDetailResponseDto> oneDaysInParticipatingInfo) {
        this.oneDaysInOperationInfo = oneDaysInOperationInfo;
        this.oneDaysInParticipatingInfo = oneDaysInParticipatingInfo;
    }
}
