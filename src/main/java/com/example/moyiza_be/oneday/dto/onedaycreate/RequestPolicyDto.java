package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestPolicyDto {
    private String genderPolicy;
    private Integer agePolicy;
}
