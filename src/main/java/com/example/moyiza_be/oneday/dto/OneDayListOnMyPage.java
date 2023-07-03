package com.example.moyiza_be.oneday.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

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
