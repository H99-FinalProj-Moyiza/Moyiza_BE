package com.example.moyiza_be.domain.user.entity;

import com.example.moyiza_be.common.enums.BasicProfileEnum;
import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.common.enums.UserRoleEnum;
import com.example.moyiza_be.common.oauth2.OAuthAttributes;
import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.domain.user.dto.SignupRequestDto;
import com.example.moyiza_be.domain.user.dto.UpdateSocialInfoRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

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
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nickname;

    private GenderEnum gender;  // 0 : MALE,  1 : FEMALE

    private Calendar birth;

    private String phone;

    private String tagString;

    @Column(name = "image_url")
    @Lob
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialLoginId; // The identifier value of the social type you are logged in as (null for normal login)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // Methods for setting user permissions
    public void authorizeUser() {
        this.role = UserRoleEnum.USER;
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
    public User (String password, SignupRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.password = password;
        this.name = requestDto.getName();
        this.nickname = requestDto.getNickname();
        this.gender = requestDto.getGender();
        this.birth = requestDto.getBirth();
        this.phone = requestDto.getPhone();
        this.profileImage = (!requestDto.getImageUrl().isEmpty()) ? requestDto.getImageUrl() : BasicProfileEnum.getRandomImage().getImageUrl();
    }

    public void setIsDeleted(Boolean flag){
        this.isDeleted = flag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setContent(String content) {
        this.content = content;
    }
}