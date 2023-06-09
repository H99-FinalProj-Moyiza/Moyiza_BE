package com.example.moyiza_be.club.dto.createclub;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRequestPolicyDto {
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;

    public void setGenderPolicy(String genderPolicy) {
        this.genderPolicy = GenderPolicyEnum.fromString(genderPolicy);
    }
}
