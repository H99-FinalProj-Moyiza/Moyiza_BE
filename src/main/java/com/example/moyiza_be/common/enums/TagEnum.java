package com.example.moyiza_be.common.enums;

public enum TagEnum {
    SOCCER("축구"),
    BASEBALL("야구"),
    WALKING("산책");

    private final String tag;

    TagEnum(String tag) {
        this.tag = tag;
    }

    public String getTag(){return this.tag;}

    public static TagEnum fromString(String tag) {
        for (TagEnum tagEnum : TagEnum.values()) {
            if (tagEnum.getTag().equalsIgnoreCase(tag)) {
                return tagEnum;
            }
        }
        throw new IllegalArgumentException(String.format("%s에 해당하는 Tag를 찾을 수 없습니다",tag));
    }
}
