package com.example.moyiza_be.blackList.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlackListMemberResponse {
    private Long blackListId;
    private String nickName;
    private String email;
    private String profileImage;

    @QueryProjection
    public BlackListMemberResponse(Long blackListId, String nickName, String email, String profileImage) {
        this.blackListId = blackListId;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
    }
}
