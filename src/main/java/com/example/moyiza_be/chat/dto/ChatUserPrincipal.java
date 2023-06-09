package com.example.moyiza_be.chat.dto;


import lombok.Getter;

import javax.security.auth.Subject;
import java.security.Principal;

@Getter
public class ChatUserPrincipal implements Principal {
    private final Long userId;
    private final String userNickname;
    private final String profileUrl;
    private Long subscribedChatId;


    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
    public ChatUserPrincipal(Long userId, String userNickname, String profileUrl, Long subscriptionChatId) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.profileUrl = profileUrl;
        this.subscribedChatId = subscriptionChatId;
    }
    public void setSubscribedChatId(Long subscribedChatId){
        this.subscribedChatId = subscribedChatId;
    }
}
