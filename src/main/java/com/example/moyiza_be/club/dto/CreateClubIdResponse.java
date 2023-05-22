package com.example.moyiza_be.club.dto;

import lombok.Getter;

@Getter
public class CreateClubIdResponse {
    private final Long createClubId;

    public CreateClubIdResponse(Long createClubId) {
        this.createClubId = createClubId;
    }
}
