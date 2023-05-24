package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;

@Getter
public class ConfirmClubCreationDto {

    private final Long ownerId;
    private final CategoryEnum category;
    private final String tagString;
    private final String title;
    private final String content;
    private final Integer maxGroupSize;
    private final GenderPolicyEnum genderPolicy;
    private final Integer agePolicy;
    private final Long createClubId;
    private final String thumbnailUrl;

    public ConfirmClubCreationDto(CreateClub createClub) {
        this.ownerId = createClub.getOwnerId();
        this.category = createClub.getCategory();
        this.tagString = createClub.getTagString();
        this.title = createClub.getTitle();
        this.content = createClub.getContent();
        this.maxGroupSize = createClub.getMaxGroupSize();
        this.genderPolicy = createClub.getGenderPolicy();
        this.agePolicy = createClub.getAgePolicy();
        this.createClubId = createClub.getId();
        this.thumbnailUrl = createClub.getThumbnailUrl();
    }
}
