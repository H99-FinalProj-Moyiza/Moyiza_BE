package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.dto.EnumOptionsDto;
import lombok.Getter;

@Getter
public class CreateClubIdResponse {
    private final Long createclub_id;
    private final EnumOptionsDto optionList= new EnumOptionsDto();

    public CreateClubIdResponse(Long createClubId) {
        this.createclub_id = createClubId;
    }
}
