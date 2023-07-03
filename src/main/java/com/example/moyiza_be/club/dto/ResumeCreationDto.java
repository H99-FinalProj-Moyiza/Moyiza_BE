package com.example.moyiza_be.club.dto;


import com.example.moyiza_be.club.dto.createclub.CreateClubResponseDto;
import com.example.moyiza_be.club.entity.CreateClub;
import lombok.Getter;

@Getter
public class ResumeCreationDto {
    private final CreateClubResponseDto createClub;
    private final EnumOptions optionList= new EnumOptions();

    public ResumeCreationDto(CreateClub createClub) {
        this.createClub = new CreateClubResponseDto(createClub);
    }

}
