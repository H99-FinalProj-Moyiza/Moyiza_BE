package com.example.moyiza_be.domain.user.dto;

import lombok.Getter;

@Getter
public class SocialInfoResponseDto {
    private String name;
    private String nickname;

    public SocialInfoResponseDto(String name, String nickname){
        this.name = name;
        this.nickname = nickname;
    }
}
