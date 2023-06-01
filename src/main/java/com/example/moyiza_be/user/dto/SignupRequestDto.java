package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.common.enums.GenderEnum;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class SignupRequestDto {

    private String name;
    private String email;
    private String password;
    private String nickname;
    private GenderEnum gender;
    private Calendar birth;
    private String phone;
}
