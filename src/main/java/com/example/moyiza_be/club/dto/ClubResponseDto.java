package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Getter
@NoArgsConstructor
public class ClubResponseDto {
    private Long id;
    private Long ownerId;
    private String title;
    private String category;
    private String tagString;
    private String content;
    private Integer agePolicy;
    private String genderPolicy;
    private Integer maxGroupSize;

    public ClubResponseDto(Club club) {
        this.id = club.getId();
        this.ownerId = club.getOwnerId();
        this.title = club.getTitle();
        this.category = club.getCategory().toString();
        this.tagString = club.getTagString();
        this.content = club.getContent();
        this.agePolicy = club.getAgePolicy();
        this.genderPolicy = club.getGenderPolicy().toString();
        this.maxGroupSize = club.getMaxGroupSize();
    }

}
