package com.example.moyiza_be.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserRoleEnum {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    GUEST("ROLE_GUEST", "임시 권한");

    private final String code;
    private final String displayName;

    public static UserRoleEnum of(String code){
        return Arrays.stream(UserRoleEnum.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
