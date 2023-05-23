package com.example.moyiza_be.common.enums;

import jdk.jfr.Category;

public enum CategoryEnum {
    SPORTS("스포츠"),
    TRAVEL("여행");

    private final String category;

    CategoryEnum(String category) {
        this.category = category;
    }

    public String getCategory(){return this.category;}

    public static CategoryEnum fromString(String category) {
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getCategory().equalsIgnoreCase(category)) {
                return categoryEnum;
            }
        }
        throw new IllegalArgumentException(String.format("%s에 해당하는 카테고리를 찾을 수 없습니다",category));
    }
}
