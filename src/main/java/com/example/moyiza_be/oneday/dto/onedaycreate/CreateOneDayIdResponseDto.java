package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.club.dto.EnumOptions;
import lombok.Getter;

@Getter
public class CreateOneDayIdResponseDto {
    private final Long createOneDayId;
    private final EnumOptions optionList = new EnumOptions();

    public CreateOneDayIdResponseDto(Long createOneDayId) {
        this.createOneDayId = createOneDayId;
    }
}
