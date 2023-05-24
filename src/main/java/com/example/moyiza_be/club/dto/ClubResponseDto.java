package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.CreateClub;
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
    private Long createclub_id;
    private Long ownerId;
    private String clubTitle;
    private String clubCategory;
    private String clubTag;
    private String clubContent;
    private Integer agePolicy;
    private String genderPolicy;
    private Integer maxGroupSize;
    private String thumbnailUrl;

    public ClubResponseDto(Club club) {
        this.createclub_id = club.getId();
        this.ownerId = club.getOwnerId();
        this.clubTitle = club.getTitle();
        this.clubCategory = club.getCategory().toString();
        this.clubTag = club.getTagString();
        this.clubContent = club.getContent();
        this.agePolicy = club.getAgePolicy();
        this.genderPolicy = club.getGenderPolicy().toString();
        this.maxGroupSize = club.getMaxGroupSize();
        this.thumbnailUrl = club.getThumbnailUrl();
    }

    public ClubResponseDto(CreateClub createClub) {
        this.createclub_id = createClub.getId();
        this.ownerId = createClub.getOwnerId();
        this.clubTitle = createClub.getTitle();
        this.clubCategory = (createClub.getCategory() != null) ? createClub.getCategory().toString() : null;
        this.clubTag = createClub.getTagString();
        this.clubContent = createClub.getContent();
        this.agePolicy = createClub.getAgePolicy();
        this.genderPolicy = (createClub.getGenderPolicy() != null) ? createClub.getGenderPolicy().toString() : null;
        this.maxGroupSize = createClub.getMaxGroupSize();
        this.thumbnailUrl = createClub.getThumbnailUrl();
    }

}
