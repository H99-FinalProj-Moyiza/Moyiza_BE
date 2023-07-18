package com.example.moyiza_be.domain.oneday.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OneDayApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long oneDayId;

    @Column
    private Long userId;

    public OneDayApproval(OneDay oneDay, Long userId) {
        this.oneDayId = oneDay.getId();
        this.userId = userId;
    }
}
