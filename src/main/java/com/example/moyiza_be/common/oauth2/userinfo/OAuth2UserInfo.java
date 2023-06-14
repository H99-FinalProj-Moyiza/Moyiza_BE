package com.example.moyiza_be.common.oauth2.userinfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //Social identification values: Google - "sub", Kakao - "id", Naver - "id"

    public abstract String getName();

    public abstract String getNickname();

    public abstract String getEmail();

    public abstract String getImageUrl();
}

