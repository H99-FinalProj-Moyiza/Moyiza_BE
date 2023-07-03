package com.example.moyiza_be.oneday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OneDayImageUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long oneDayCreateId;
    private Long oneDayId;
    private String imageUrl;

    public OneDayImageUrl(Long oneDayCreateId, String imageUrl) {
        this.oneDayCreateId = oneDayCreateId;
        this.imageUrl = imageUrl;
    }

}
