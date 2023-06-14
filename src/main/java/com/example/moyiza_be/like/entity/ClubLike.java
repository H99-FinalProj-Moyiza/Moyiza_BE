package com.example.moyiza_be.like.entity;

import com.example.moyiza_be.common.enums.LikeTypeEnum;
import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ClubLike extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long clubId;

    public ClubLike(Long userId, Long clubId) {
        this.clubId = clubId;
        this.userId = userId;
    }
}
