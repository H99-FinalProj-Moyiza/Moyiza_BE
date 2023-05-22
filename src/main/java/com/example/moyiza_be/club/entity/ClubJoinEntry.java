package com.example.moyiza_be.club.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ClubJoinEntry {
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
