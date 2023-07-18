package com.example.moyiza_be.domain.oneday.dto.onedaycreate;

import com.example.moyiza_be.domain.club.dto.EnumOptions;
import com.example.moyiza_be.domain.oneday.entity.OneDayCreate;
import lombok.Getter;

@Getter
public class CreatingDto {
    private final OneDayCreatingResponseDto oneDayCreatingResponseDto;
    private final EnumOptions optionList = new EnumOptions();

    public CreatingDto(OneDayCreate oneDayCreate) {
        this.oneDayCreatingResponseDto = new OneDayCreatingResponseDto(oneDayCreate);
    }
}
