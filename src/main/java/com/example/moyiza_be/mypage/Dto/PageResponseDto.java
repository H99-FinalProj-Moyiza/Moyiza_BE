package com.example.moyiza_be.mypage.Dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.user.entity.User;

import java.util.List;

public class PageResponseDto {
    private User user;
    private List<Club> clubList;

    public PageResponseDto(User user, List<Club> clubList) {
        this.user = user;
        this.clubList = clubList;
    }
}
