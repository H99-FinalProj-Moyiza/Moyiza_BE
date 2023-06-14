package com.example.moyiza_be.club.entity;

import com.example.moyiza_be.club.dto.ConfirmClubCreationDto;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Club extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;
    @Column(nullable = false)
    private Long ownerId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
    @Column(nullable = false)
    private String tagString;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Integer agePolicy;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderPolicyEnum genderPolicy;
    @Column(nullable = false)
    private Integer maxGroupSize;
    @Column(nullable = false)
    private String thumbnailUrl;
    @Column
    private Integer nowMemberCount = 0;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column
    private Integer numLikes = 0;

    public Club(ConfirmClubCreationDto creationRequest) {
        this.ownerId = creationRequest.getOwnerId();
        this.title = creationRequest.getTitle();
        this.category = creationRequest.getCategory();
        this.tagString = creationRequest.getTagString();
        this.content = creationRequest.getContent();
        this.agePolicy = creationRequest.getAgePolicy();
        this.genderPolicy = creationRequest.getGenderPolicy();
        this.maxGroupSize = creationRequest.getMaxGroupSize();
        this.thumbnailUrl = creationRequest.getThumbnailUrl();
    }


    public void flagDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }

    public void addAttend() {
        this.nowMemberCount++;
    }

    public void cancelAttend() {
        this.nowMemberCount--;
    }

    public void addLike(){
        if(this.numLikes == null){this.numLikes = 0;}
        this.numLikes++;
    }
    public void minusLike(){
        this.numLikes--;
    }
}
