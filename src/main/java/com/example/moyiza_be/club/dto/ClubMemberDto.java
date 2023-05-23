package com.example.moyiza_be.club.dto;

import lombok.Getter;

@Getter
public class ClubMemberDto {
    private Long userId;
    private String memberNickname;
    private String memberEmail;

    public ClubMemberDto(Long userId, String memberNickname,
                         String memberEmail) {
        this.userId = userId;
        this.memberNickname = memberNickname;
        this.memberEmail = memberEmail;
    }

}
