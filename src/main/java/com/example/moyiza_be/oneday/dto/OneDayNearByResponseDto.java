package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.oneday.entity.OneDay;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OneDayNearByResponseDto {
    private OneDay oneDay;
    private double distance;
}
