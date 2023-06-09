package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ClubMemberResponse {
    private final Long userId;
    private final String userNickname;
    private final String profilePictureUrl;
    private final LocalDateTime joinedSince;

    public ClubMemberResponse(User user, LocalDateTime joinedSince) {
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.profilePictureUrl = user.getProfileImage();
        this.joinedSince = joinedSince;
    }

    @QueryProjection
    public ClubMemberResponse(Long userId, String userNickname, String profilePictureUrl, LocalDateTime joinedSince){
        this.userId = userId;
        this.userNickname = userNickname;
        this.profilePictureUrl = profilePictureUrl;
        this.joinedSince = joinedSince;
    }
}
