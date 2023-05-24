package com.example.moyiza_be.common.enums;

public enum GenderPolicyEnum {
    M("남자만"),
    F("여자만"),
    MF("남녀모두");

    private final String genderPolicy;

    GenderPolicyEnum(String genderPolicy) {
        this.genderPolicy = genderPolicy;
    }

    public String getGenderPolicy(){return this.genderPolicy;}

    public static GenderPolicyEnum fromString(String policy) {
        for (GenderPolicyEnum genderPolicyEnum : GenderPolicyEnum.values()) {
            if (genderPolicyEnum.getGenderPolicy().equalsIgnoreCase(policy)) {
                return genderPolicyEnum;
            }
        }
        throw new IllegalArgumentException(String.format("%s에 해당하는 Policy를 찾을 수 없습니다",policy));
    }
}
