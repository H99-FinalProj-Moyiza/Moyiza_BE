package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public OneDayAttendant(Long OneDayId, Long userId) {
        this.oneDayId = OneDayId;
        this.userId = userId;
    }
}
