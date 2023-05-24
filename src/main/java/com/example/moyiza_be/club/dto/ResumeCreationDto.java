package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.dto.createclub.CreateClubResponse;
import com.example.moyiza_be.club.entity.CreateClub;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ResumeCreationDto {
    private final ClubResponseDto createClub;

    private final EnumOptionsDto optionList= new EnumOptionsDto();

    public ResumeCreationDto(CreateClub createClub) {
        this.createClub = new ClubResponseDto(createClub);
    }

}
