package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.dto.createclub.CreateClubResponse;
import com.example.moyiza_be.club.entity.CreateClub;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ResumeCreationDto {
    private final CreateClubResponse createClubResponse;

    private final EnumOptionsDto optionList= new EnumOptionsDto();

    public ResumeCreationDto(CreateClub createClub) {
        this.createClubResponse = new CreateClubResponse(createClub);
    }

}
