package com.example.moyiza_be.common.oauth2;

import com.example.moyiza_be.common.enums.SocialType;
import com.example.moyiza_be.common.enums.UserRoleEnum;
import com.example.moyiza_be.common.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.example.moyiza_be.common.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.example.moyiza_be.common.oauth2.userinfo.NaverOAuth2UserInfo;
import com.example.moyiza_be.common.oauth2.userinfo.OAuth2UserInfo;
import com.example.moyiza_be.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oauth2Userinfo;
    private PasswordEncoder passwordEncoder;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2Userinfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType providerType,
                                     String userNameAttributeName, Map<String, Object> attributes){
        if(providerType == providerType.NAVER){
            return ofNaver(userNameAttributeName, attributes);
        }
        if (providerType == providerType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }
    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialLoginId(oAuth2UserInfo.getId())
                .name("TempName") //temp
                .email(oAuth2UserInfo.getEmail())
                .nickname(UUID.randomUUID().toString()) //temp
                .profileImage(oAuth2UserInfo.getImageUrl())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRoleEnum.GUEST)
                .build();
    }
}
