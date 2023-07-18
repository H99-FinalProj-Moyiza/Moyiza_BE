package com.example.moyiza_be.domain.club.dto;

import lombok.Getter;

@Getter
public class CreateClubIdResponse {
    private final Long createclub_id;
    private final EnumOptions optionList= new EnumOptions();

    public CreateClubIdResponse(Long createClubId) {
        this.createclub_id = createClubId;
    }
}
