package com.example.moyiza_be.domain.like.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OnedayLike extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long onedayId;

    public OnedayLike(Long userId, Long onedayId) {
        this.userId = userId;
        this.onedayId = onedayId;
    }
}
