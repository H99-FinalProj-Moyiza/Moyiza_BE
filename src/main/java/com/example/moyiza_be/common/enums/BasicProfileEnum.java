package com.example.moyiza_be.common.enums;

import java.util.Random;

public enum BasicProfileEnum {
    DIPSY("https://moyiza-image.s3.ap-northeast-2.amazonaws.com/0ba71500-7bb2-430f-9d4f-b88c7484bad6_BasicProfile_1.png"),
    PO("https://moyiza-image.s3.ap-northeast-2.amazonaws.com/63e0437b-864b-4e2f-bebb-549c6ae37bd6_BasicProfile_2.png"),
    TINKYWINKY("https://moyiza-image.s3.ap-northeast-2.amazonaws.com/f1d3fce1-1eaf-4dde-a054-5241775cd2ff_BasicProfile_3.png"),
    LAALAA("https://moyiza-image.s3.ap-northeast-2.amazonaws.com/ce8ac041-1be6-47ff-898e-ccdd3045a50d_BasicProfile_4.png");

    private final String imageUrl;

    BasicProfileEnum(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public static BasicProfileEnum getRandomImage() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
