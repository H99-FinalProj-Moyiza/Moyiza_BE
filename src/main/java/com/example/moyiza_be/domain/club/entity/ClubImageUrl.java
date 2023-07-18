package com.example.moyiza_be.domain.club.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ClubImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long createClubId;
    private Long clubId = -1L;
    private String imageUrl;

    public ClubImageUrl(Long createClubId, String imageUrl) {
        this.createClubId = createClubId;
        this.imageUrl = imageUrl;
    }

    public ClubImageUrl(Long createClubId,Long clubId, String imageUrl){
        this.createClubId = createClubId;
        this.clubId = clubId;
        this.imageUrl = imageUrl;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }
}
