package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestPolicyDto {
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    public void setGenderPolicy(String genderPolicy) {
        this.agePolicy = GenderPolicyEnum.fromString(genderPolicy).ordinal();
    }
}
