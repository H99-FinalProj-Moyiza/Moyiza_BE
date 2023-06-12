package com.example.moyiza_be.oneday.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OneDayListOnMyPage {
    private List<OneDayDetailOnMyPage> oneDaysInOperationInfo;
    private List<OneDayDetailOnMyPage> oneDaysInParticipatingInfo;

    public OneDayListOnMyPage(List<OneDayDetailOnMyPage> oneDaysInOperationInfo,
                              List<OneDayDetailOnMyPage> oneDaysInParticipatingInfo) {
        this.oneDaysInOperationInfo = oneDaysInOperationInfo;
        this.oneDaysInParticipatingInfo = oneDaysInParticipatingInfo;
    }
}
