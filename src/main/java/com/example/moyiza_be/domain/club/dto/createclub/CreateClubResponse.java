package com.example.moyiza_be.domain.club.dto.createclub;


import com.example.moyiza_be.domain.club.entity.CreateClub;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateClubResponse {

    private Long createclub_id;
    private Long ownerId;
    private String clubTitle;
    private CategoryEnum clubCategory;
    private String clubTag;
    private String content;
    private Integer agePolicy;
    private GenderPolicyEnum genderPolicy;
    private Integer maxGroupSize;
    private String thumbnailUrl;

    public CreateClubResponse(CreateClub createClub) {
        this.createclub_id = createClub.getId();
        this.ownerId = createClub.getOwnerId();
        this.clubTitle = createClub.getTitle();
        this.clubCategory = createClub.getCategory();
        this.clubTag = createClub.getTagString();
        this.content = createClub.getContent();
        this.agePolicy = createClub.getAgePolicy();
        this.genderPolicy = createClub.getGenderPolicy();
        this.maxGroupSize = createClub.getMaxGroupSize();
        this.thumbnailUrl = createClub.getThumbnailUrl();

    }
}
