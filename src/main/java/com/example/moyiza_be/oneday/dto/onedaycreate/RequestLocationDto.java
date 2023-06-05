package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class RequestLocationDto {
    private String OneDayLocation;
    private String OneDayLatitude;
    private String OneDayLongitude;
}
