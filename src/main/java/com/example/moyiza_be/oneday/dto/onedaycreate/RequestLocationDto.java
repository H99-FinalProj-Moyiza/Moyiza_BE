package com.example.moyiza_be.oneday.dto.onedaycreate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class RequestLocationDto {
    private String OneDayLocation;
    private double OneDayLatitude;
    private double OneDayLongitude;
}
