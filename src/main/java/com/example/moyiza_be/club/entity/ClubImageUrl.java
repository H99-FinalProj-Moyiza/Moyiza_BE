package com.example.moyiza_be.club.entity;

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
        private Long clubId;
        private String imageUrl;

    public ClubImageUrl(Long createClubId, String imageUrl) {
            this.createClubId = createClubId;
            this.imageUrl = imageUrl;
        }

        public void setClubId(Long clubId) {
        this.clubId = clubId;
    }
}
