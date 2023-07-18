package com.example.moyiza_be.domain.oneday.dto;

import lombok.Getter;

@Getter
public class OneDayIdResponseDto {
    private final Long createOneDayId;

    public OneDayIdResponseDto(Long createOneDayId) {
        this.createOneDayId = createOneDayId;
    }
}
