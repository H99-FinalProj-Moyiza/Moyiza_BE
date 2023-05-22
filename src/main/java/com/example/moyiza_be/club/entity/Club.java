package com.example.moyiza_be.club.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity
@Getter
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    private String title;
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
    private String content;
    private Calendar allowBirthBefore;
    @Enumerated(EnumType.STRING)
    private
}
