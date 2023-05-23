package com.example.moyiza_be.user.dto;

import lombok.Getter;

import java.util.Calendar;

@Getter
public class SignupRequestDto {

    private String name;
    private String email;
    private String password;
    private String nickname;
    private int gender;
    private Calendar birth;
    private String phone;
    private String profileUrl;
}
