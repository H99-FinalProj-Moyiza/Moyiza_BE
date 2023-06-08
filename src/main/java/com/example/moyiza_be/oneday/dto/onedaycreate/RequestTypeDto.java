package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestTypeDto {
    private String oneDayType;

    public OneDayTypeEnum getOneDayTypeEnum() { return OneDayTypeEnum.fromString(this.oneDayType);}
}
