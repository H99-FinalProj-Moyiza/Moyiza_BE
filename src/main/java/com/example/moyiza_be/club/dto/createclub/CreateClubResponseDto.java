package com.example.moyiza_be.club.dto.createclub;

import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateClubResponseDto {
    private final Long createclub_id;
    private final Long ownerId;
    private final String clubTitle;
    private final String clubCategory;
    private final List<String> clubTag;
    private final String clubContent;
    private final Integer agePolicy;
    private final String genderPolicy;
    private final Integer maxGroupSize;
    private final String thumbnailUrl;

    public CreateClubResponseDto(CreateClub createClub) {
        this.createclub_id = createClub.getId();
        this.ownerId = createClub.getOwnerId();
        this.clubTitle = createClub.getTitle();
        this.clubCategory = (createClub.getCategory() != null) ? createClub.getCategory().getCategory() : null;
        this.clubTag = TagEnum.parseTag(createClub.getTagString());
        this.clubContent = createClub.getContent();
        this.agePolicy = createClub.getAgePolicy();
        this.genderPolicy = (createClub.getGenderPolicy() != null) ? createClub.getGenderPolicy().getGenderPolicy() : null;
        this.maxGroupSize = createClub.getMaxGroupSize();
        this.thumbnailUrl = createClub.getThumbnailUrl();
    }
}
