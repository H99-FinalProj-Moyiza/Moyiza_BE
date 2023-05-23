package com.example.moyiza_be.club.dto;

import lombok.Getter;

@Getter
public class CreateClubIdResponse {
    private final Long createClubId;
    private final EnumOptionsDto optionList= new EnumOptionsDto();

    public CreateClubIdResponse(Long createClubId) {
        this.createClubId = createClubId;
    }
}
