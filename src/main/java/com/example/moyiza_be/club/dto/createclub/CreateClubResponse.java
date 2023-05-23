package com.example.moyiza_be.club.dto.createclub;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateClubResponse {
    private Long id;
    private Long ownerId;
    private String title;
    private CategoryEnum category;
    private String tagString;
    private String content;
    private Integer agePolicy;
    private GenderPolicyEnum genderPolicy;
    private Integer maxGroupSize;

    public CreateClubResponse(CreateClub createClub) {
        this.id = createClub.getId();
        this.ownerId = createClub.getOwnerId();
        this.title = createClub.getTitle();
        this.category = createClub.getCategory();
        this.tagString = createClub.getTagString();
        this.content = createClub.getContent();
        this.agePolicy = createClub.getAgePolicy();
        this.genderPolicy = createClub.getGenderPolicy();
        this.maxGroupSize = createClub.getMaxGroupSize();
    }
}
