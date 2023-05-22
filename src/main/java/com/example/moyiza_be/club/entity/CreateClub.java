package com.example.moyiza_be.club.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CreateClub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
