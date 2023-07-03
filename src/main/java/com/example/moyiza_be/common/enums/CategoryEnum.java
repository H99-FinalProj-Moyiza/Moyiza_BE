package com.example.moyiza_be.common.enums;

public enum CategoryEnum {

    WORKOUT("운동"),

    SPORTS("스포츠"),

    TRAVEL("여행"),

    CULTURE("문화"),

    ART("예술"),

    ACTIVITY("액티비티"),

    LOVE("연애"),

    SELFDEV("자기계발"),

    HOBBY("취미");





    private final String category;

    CategoryEnum(String category) {
        this.category = category;
    }

    public String getCategory(){return this.category;}

    public static CategoryEnum fromString(String category) {
        if (category == null) return null;
        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            if (categoryEnum.getCategory().equalsIgnoreCase(category)) {
                return categoryEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No category was found for %s",category));
    }
}
