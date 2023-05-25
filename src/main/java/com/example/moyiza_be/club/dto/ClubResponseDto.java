package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.CreateClub;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClubResponseDto {
    private Long club_id;
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
        this.club_id = club.getId();
        this.ownerId = club.getOwnerId();
        this.clubTitle = club.getTitle();
        this.clubCategory = club.getCategory().getCategory();
        this.clubTag = club.getTagString();
        this.clubContent = club.getContent();
        this.agePolicy = club.getAgePolicy();
        this.genderPolicy = club.getGenderPolicy().getGenderPolicy();
        this.maxGroupSize = club.getMaxGroupSize();
        this.thumbnailUrl = club.getThumbnailUrl();
    }

}
