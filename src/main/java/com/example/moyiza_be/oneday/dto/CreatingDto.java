package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.club.dto.EnumOptions;
import com.example.moyiza_be.oneday.entity.OneDayCreate;
import lombok.Getter;

@Getter
public class CreatingDto {
    private final OneDayCreateResponseDto oneDayCreateResponseDto;
    private final EnumOptions optionList = new EnumOptions();

    public CreatingDto(OneDayCreate oneDayCreate) {
        this.oneDayCreateResponseDto = new OneDayCreateResponseDto(oneDayCreate);
    }
}
