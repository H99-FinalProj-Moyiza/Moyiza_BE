package com.example.moyiza_be.mypage.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PageResponseDto {
    private UserResponseDto userInfo;
    private List<ClubResponseDto> clubsInOperationInfo;
    private List<ClubResponseDto> clubsInParticipatingInfo;

    public PageResponseDto(UserResponseDto userInfo, List<ClubResponseDto> clubsInOperationInfo, List<ClubResponseDto> clubsInParticipatingInfo) {
        this.userInfo = userInfo;
        this.clubsInOperationInfo = clubsInOperationInfo;
        this.clubsInParticipatingInfo = clubsInParticipatingInfo;
    }
}
