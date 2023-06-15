package com.example.moyiza_be.common.oauth2;

import org.springframework.security.core.AuthenticationException;

public class OAuthSocialTypeMissMatchException extends AuthenticationException {

    public OAuthSocialTypeMissMatchException(String message) {
        super(message);
    }
}