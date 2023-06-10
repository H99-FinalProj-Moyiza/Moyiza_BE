package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoOnMyPage {
    private Long user_id;
    private String nickname;
    private String email;
    private String profileImage;
    private Integer clubsInOperationCount;
    private Integer clubsInParticipatingCount;
    private Integer oneDaysInOperationCount;
    private Integer oneDaysInParticipatingCount;

    public UserInfoOnMyPage(User user, Integer clubsInOperationCount, Integer clubsInParticipatingCount,
                            Integer oneDaysInOperationCount, Integer oneDaysInParticipatingCount) {
        this.user_id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.clubsInOperationCount = clubsInOperationCount;
        this.clubsInParticipatingCount = clubsInParticipatingCount;
        this.oneDaysInOperationCount = oneDaysInOperationCount;
        this.oneDaysInParticipatingCount = oneDaysInParticipatingCount;
    }
}
