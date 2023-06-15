package com.example.moyiza_be.club.dto;

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
