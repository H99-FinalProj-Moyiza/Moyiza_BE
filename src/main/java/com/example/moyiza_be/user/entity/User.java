package com.example.moyiza_be.user.entity;

import com.example.moyiza_be.common.enums.BasicProfileEnum;
import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.common.oauth2.OAuthAttributes;
import com.example.moyiza_be.common.oauth2.Role;
import com.example.moyiza_be.common.utils.TimeStamped;

import com.example.moyiza_be.user.dto.SignupRequestDto;
import com.example.moyiza_be.user.dto.TestSignupRequestDto;
import com.example.moyiza_be.user.dto.UpdateRequestDto;
import com.example.moyiza_be.user.dto.UpdateSocialInfoRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Entity(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String nickname;

    private GenderEnum gender;  // 0 : 남자,  1 : 여자

    private Calendar birth;

    private String phone;

    private String tagString;

    @Column(name = "image_url")
    @Lob
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialLoginId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

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

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void updateProfile(String nickname, String tagString){
        this.nickname = nickname;
        this.tagString = tagString;
    }
    public void updateProfileImage(String storedFileName) {
        this.profileImage = storedFileName;
    }

    public User (String nickName, Long id){
        this.nickname = nickName;
        this.id = id;
    }

    public void updateSocialLogin(OAuthAttributes attributes, SocialType socialType){
        this.socialLoginId = attributes.getOauth2Userinfo().getId();
        this.socialType = socialType;
    }

    public void updateSocialInfo(UpdateSocialInfoRequestDto requestDto) {
        this.name = requestDto.getName();
        this.nickname = requestDto.getNickname();
        this.gender = requestDto.getGender();
        this.birth = requestDto.getBirth();
        this.phone = requestDto.getPhone();
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
        this.profileImage = (!requestDto.getImageUrl().isEmpty()) ? requestDto.getImageUrl() : BasicProfileEnum.getRandomImage().getImageUrl();
    }
}