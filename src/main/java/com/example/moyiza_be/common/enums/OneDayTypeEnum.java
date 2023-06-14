package com.example.moyiza_be.common.enums;

public enum OneDayTypeEnum {
    APPROVAL("승인형"),
    FCFSB("선착순형");

    private final String oneDayType;

    OneDayTypeEnum(String oneDayTypeEnum) {
        this.oneDayType = oneDayTypeEnum;
    }


    public String getOneDayType(){return this.oneDayType;}

    public static OneDayTypeEnum fromString(String policy) {
        for (OneDayTypeEnum oneDayTypeEnum : OneDayTypeEnum.values()) {
            if (oneDayTypeEnum.getOneDayType().equalsIgnoreCase(policy)) {
                return oneDayTypeEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No type found for %s", policy));
    }
}
