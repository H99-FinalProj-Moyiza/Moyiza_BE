package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OneDayMemberResponse {
    private final Long userId;
    private final String userNickname;
    private final String profilePictureUrl;

    public OneDayMemberResponse(User user) {
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.profilePictureUrl = user.getProfileImage();
    }

    @QueryProjection
    public OneDayMemberResponse(Long userId, String userNickname, String profilePictureUrl) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.profilePictureUrl = profilePictureUrl;
    }
}
