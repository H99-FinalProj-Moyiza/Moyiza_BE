package com.example.moyiza_be.club.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ClubJoinEntry extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long clubId;

    public ClubJoinEntry(Long userId, Long clubId) {
        this.userId = userId;
        this.clubId = clubId;
    }
}
