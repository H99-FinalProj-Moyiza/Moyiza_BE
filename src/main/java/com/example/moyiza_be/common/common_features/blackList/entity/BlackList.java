package com.example.moyiza_be.common.common_features.blackList.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BlackList extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="blackList_id")
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long blackListUserId;

    public BlackList(Long userId, Long profileId) {
        this.userId = userId;
        this.blackListUserId = profileId;
    }
}
