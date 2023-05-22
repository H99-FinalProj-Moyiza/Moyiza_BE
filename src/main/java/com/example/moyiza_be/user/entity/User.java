package com.example.moyiza_be.user.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private String nickname;
    private Integer gender;  // 0 : 여성,  1 : 남성
    private Calendar birth;
    private String phone;
    private String profileUrl;

    public User (String password, SignupRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = password;
        this.name = requestDto.getName();
        this.nickname = requestDto.getNickname();
        this.gender = requestDto.getGender();
        this.birth = requestDto.getBirth();
        this.phone = requestDto.getPhone();
        this.profileUrl = requestDto.getProfileUrl();
    }
}