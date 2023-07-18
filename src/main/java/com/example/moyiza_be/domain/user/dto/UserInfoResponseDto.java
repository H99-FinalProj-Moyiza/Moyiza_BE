package com.example.moyiza_be.domain.user.dto;

import com.example.moyiza_be.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;

    public UserInfoResponseDto(User user){
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
