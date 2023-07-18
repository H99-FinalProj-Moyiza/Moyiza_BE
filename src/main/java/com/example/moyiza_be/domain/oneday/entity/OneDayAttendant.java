package com.example.moyiza_be.domain.oneday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OneDayAttendant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long oneDayId;

    @Column
    private Long userId;

    public OneDayAttendant(OneDay OneDay, Long userId) {
        this.oneDayId = OneDay.getId();
        this.userId = userId;
    }
}
