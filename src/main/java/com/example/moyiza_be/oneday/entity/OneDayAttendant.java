package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.user.entity.User;
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
