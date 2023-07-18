package com.example.moyiza_be.domain.user.dto;

import com.example.moyiza_be.common.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class UpdateSocialInfoRequestDto {

    private String name;
    private String nickname;
    private GenderEnum gender;
    private Calendar birth;
    private String phone;
}
