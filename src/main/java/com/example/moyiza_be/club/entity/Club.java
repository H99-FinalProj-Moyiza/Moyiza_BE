package com.example.moyiza_be.club.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private User ownerId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Calendar allowBirthBefore;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum allowGender;
    @Column(nullable = false)
    private Boolean requireApproval;
    @Column(nullable = false)
    private Integer maxGroupSize;
}
