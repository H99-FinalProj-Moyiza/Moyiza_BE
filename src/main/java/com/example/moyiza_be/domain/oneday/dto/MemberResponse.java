package com.example.moyiza_be.domain.oneday.dto;

import com.example.moyiza_be.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long userId;
    private final String userNickname;
    private final String profilePictureUrl;

    public MemberResponse(User user) {
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.profilePictureUrl = user.getProfileImage();
    }

    @QueryProjection
    public MemberResponse(Long userId, String userNickname, String profilePictureUrl) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.profilePictureUrl = profilePictureUrl;
    }
}
