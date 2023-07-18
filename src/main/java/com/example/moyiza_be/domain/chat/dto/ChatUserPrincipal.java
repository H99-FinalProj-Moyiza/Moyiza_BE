package com.example.moyiza_be.domain.chat.dto;


import lombok.Getter;

import javax.security.auth.Subject;
import java.security.Principal;

@Getter
public class ChatUserPrincipal implements Principal {
    private final Long userId;
    private final String userNickname;
    private final String profileUrl;


    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
    public ChatUserPrincipal(Long userId, String userNickname, String profileUrl) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.profileUrl = profileUrl;
    }
}
