package com.example.moyiza_be.common.enums;

public enum UserRoleEnum {
    ROLE_GUEST(Authorization.GUEST),
    ROLE_USER(Authorization.USER),
    ROLE_ADMIN(Authorization.ADMIN);

    private final String authorization;

    private UserRoleEnum(String authorization){
        this.authorization = authorization;
    }
    public String getAuthorization(){
        return authorization;
    }

    private class Authorization{
        private static final String GUEST = "GUEST";
        private static final String USER = "USER";
        private static final String ADMIN = "ADMIN";
    }
}
