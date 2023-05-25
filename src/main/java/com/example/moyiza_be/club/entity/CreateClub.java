package com.example.moyiza_be.club.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CreateClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private CategoryEnum category;

    private String tagString;

    private String title;

    private String content;

    private GenderPolicyEnum genderPolicy;

    private Integer agePolicy;

    private Integer maxGroupSize;

    private String thumbnailUrl;


    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGenderPolicy(GenderPolicyEnum genderPolicy) {
        this.genderPolicy = genderPolicy;
    }

    public void setAgePolicy(Integer agePolicy) {
        this.agePolicy = agePolicy;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


}

