package com.example.moyiza_be.user.entity;

import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.common.utils.TimeStamped;

import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.dto.TestSignupRequestDto;
import com.example.moyiza_be.user.dto.UpdateRequestDto;
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
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private GenderEnum gender;  // 0 : 남자,  1 : 여자
    @Column(nullable = false)
    private Calendar birth;
    @Column(nullable = false)
    private String phone;
    @Column(name = "image_url")
    @Lob
    private String profileImage;

    public User (String password, SignupRequestDto requestDto, String storedFileUrl){
        this.email = requestDto.getEmail();
        this.password = password;
        this.name = requestDto.getName();
        this.nickname = requestDto.getNickname();
        this.gender = requestDto.getGender();
        this.birth = requestDto.getBirth();
        this.phone = requestDto.getPhone();
        this.profileImage = storedFileUrl;
    }

    //테스트
    public User (String password, TestSignupRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = password;
        this.name = requestDto.getName();
        this.nickname = requestDto.getNickname();
        this.gender = requestDto.getGender();
        this.birth = requestDto.getBirth();
        this.phone = requestDto.getPhone();
        this.profileImage = requestDto.getImageUrl();
    }

    public void updateProfile(UpdateRequestDto requestDto){
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
    }

    public void updateProfileImage(String storedFileName) {
        this.profileImage = storedFileName;
    }
}