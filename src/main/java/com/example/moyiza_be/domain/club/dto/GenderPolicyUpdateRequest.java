package com.example.moyiza_be.domain.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenderPolicyUpdateRequest {
    private String genderPolicy;

    public GenderPolicyUpdateRequest(String genderPolicy) {
        this.genderPolicy = genderPolicy;
    }
}
