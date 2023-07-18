package com.example.moyiza_be.domain.club.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AgePolicyUpdateRequest {
    private Integer agePolicy;

    public AgePolicyUpdateRequest(Integer agePolicy) {
        this.agePolicy = agePolicy;
    }
}
