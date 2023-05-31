package com.example.moyiza_be.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ChatUserInfo {
    private final String nickName;
    private final Long userId;
    private final String profileUrl;

    public ChatUserInfo(Long userId, String nickName, String profileUrl) {
        this.nickName = nickName;
        this.userId = userId;
        this.profileUrl = profileUrl;
    }
}
