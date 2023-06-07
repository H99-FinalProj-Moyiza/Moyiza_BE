package com.example.moyiza_be.mypage.Dto;

import com.example.moyiza_be.user.entity.User;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long user_id;
    private String nickname;
    private String profileImage;
    private Integer clubsInOperationCount;
    private Integer clubsInParticipatingCount;

    public UserResponseDto(User user) {
        this.user_id = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
