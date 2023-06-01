package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.user.entity.User;
import lombok.Getter;

@Getter
public class ChatUserInfo {
    private final Long userId;
    private final String userNickname;
    private final String profileUrl;

    public ChatUserInfo(User user) {
        this.userId = user.getId();
        this.userNickname = user.getNickname();
        this.profileUrl = user.getProfileImage();
    }

    public ChatUserInfo(Long userId, String userNickname, String profileUrl) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.profileUrl = profileUrl;
    }
}
