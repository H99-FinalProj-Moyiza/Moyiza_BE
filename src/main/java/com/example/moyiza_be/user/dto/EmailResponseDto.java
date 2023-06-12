package com.example.moyiza_be.user.dto;

import lombok.Getter;

@Getter
public class EmailResponseDto {
    private String userEmail;

    public EmailResponseDto(String userEmail){
        this.userEmail = userEmail;
    }
}
