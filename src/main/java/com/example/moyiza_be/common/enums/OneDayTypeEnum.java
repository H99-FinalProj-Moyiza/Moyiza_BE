package com.example.moyiza_be.common.enums;

public enum OneDayTypeEnum {
    APPROVAL("승인형"),
    FCFSB("선착순형");

    private final String oneDayTypeEnum;

    OneDayTypeEnum(String oneDayTypeEnum) {
        this.oneDayTypeEnum = oneDayTypeEnum;
    }


    public String getOneDayTypeEnum(){return this.oneDayTypeEnum;}

    public static OneDayTypeEnum fromString(String policy) {
        for (OneDayTypeEnum oneDayTypeEnum : OneDayTypeEnum.values()) {
            if (oneDayTypeEnum.getOneDayTypeEnum().equalsIgnoreCase(policy)) {
                return oneDayTypeEnum;
            }
        }
        throw new IllegalArgumentException(String.format("%s에 해당하는 타입을 찾을 수 없습니다",policy));
    }
}
