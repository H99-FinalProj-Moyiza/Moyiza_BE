package com.example.moyiza_be.domain.user.dto;

import com.example.moyiza_be.common.enums.GenderEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter
@Setter
public class SignupRequestDto {

    private String name;
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z\\d$@!%*?&()_]{8,16}$",
            message = "비밀번호는 최소 8자 이상 16자 이하이며 알파벳 영문, 숫자가 포함되어야 합니다.")
    private String password;
    private String nickname;
    private GenderEnum gender;
    private Calendar birth;
    private String phone;
    private String imageUrl;
}