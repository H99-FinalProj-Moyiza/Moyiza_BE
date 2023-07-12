package com.example.moyiza_be.common.enums;

import net.nurigo.sdk.message.model.Count;

import java.util.ArrayList;
import java.util.List;

public enum TagEnum {

    // Work Out
    FITNESS("헬스", CategoryEnum.WORKOUT),
    PILATES("필라테스", CategoryEnum.WORKOUT),
    YOGA("요가", CategoryEnum.WORKOUT),
    RUNNING("런닝", CategoryEnum.WORKOUT),
    HIKING("등산", CategoryEnum.WORKOUT),
    SURFING("서핑", CategoryEnum.WORKOUT),
    OUTCLIMBING("암벽등반", CategoryEnum.WORKOUT),
    CROSSFIT("크로스핏", CategoryEnum.WORKOUT),
    JUMPROPE("줄넘기", CategoryEnum.WORKOUT),

    // Sports
    SOCCER("축구", CategoryEnum.SPORTS),
    BASKETBALL("농구", CategoryEnum.SPORTS),
    BASEBALL("야구", CategoryEnum.SPORTS),
    SWIM("수영", CategoryEnum.SPORTS),
    MARATHON("마라톤", CategoryEnum.SPORTS),
    PINGPONG("탁구", CategoryEnum.SPORTS),
    WATERSPORTS("수상스포츠", CategoryEnum.SPORTS),
    BADMINTON("배드민턴", CategoryEnum.SPORTS),
    CLIMBING("클라이밍", CategoryEnum.SPORTS),
    CYCLING("사이클링", CategoryEnum.SPORTS),

    // Travel
    OVERSEAS("해외여행", CategoryEnum.TRAVEL),
    DOMESTIC("국내여행", CategoryEnum.TRAVEL),
    ONEDAY("당일여행", CategoryEnum.TRAVEL),
    GAMSUNG("감성여행", CategoryEnum.TRAVEL),
    THEME("테마여행", CategoryEnum.TRAVEL),
    CARTRIP("차박", CategoryEnum.TRAVEL),
    CAMPING("캠핑", CategoryEnum.TRAVEL),
    ADVENTURE("오지여행", CategoryEnum.TRAVEL),
    JEJU("제주여행", CategoryEnum.TRAVEL),

    // Culture
    MOVIE("영화", CategoryEnum.CULTURE),
    CONCERT("공연", CategoryEnum.CULTURE),
    PHOTO("사진", CategoryEnum.CULTURE),
    LITERATURE("문학", CategoryEnum.CULTURE),
    BROADCAST("방송", CategoryEnum.CULTURE),
    DESIGN("디자인", CategoryEnum.CULTURE),
    RELIGION("종교", CategoryEnum.CULTURE),
    FESTIVAL("축제", CategoryEnum.CULTURE),
    HISTORY("역사", CategoryEnum.CULTURE),

    //Arts
    DRAWING("그림", CategoryEnum.ART),
    MUSIC("음악", CategoryEnum.ART),
    EXHIBIT("전시", CategoryEnum.ART),
    CALLIGRAPHY("캘리그라피", CategoryEnum.ART),
    HANDCRAFT("공예", CategoryEnum.ART),
    THEATER("연극", CategoryEnum.ART),
    MUSICAL("뮤지컬", CategoryEnum.ART),
    DANCE("댄스", CategoryEnum.ART),
    WRITING("서예", CategoryEnum.ART),

    // Activity
    TRACKING("트래킹", CategoryEnum.ACTIVITY),
    FISHING("낚시", CategoryEnum.ACTIVITY),
    BOARD("보드", CategoryEnum.ACTIVITY),
    DIVING("스쿠버다이빙", CategoryEnum.ACTIVITY),
    DRIVE("드라이브", CategoryEnum.ACTIVITY),
    THEMEPARK("테마파크", CategoryEnum.ACTIVITY),
    PARAGLIDING("페러글라이딩", CategoryEnum.ACTIVITY),
    PICNIC("피크닉", CategoryEnum.ACTIVITY),
    OUTDOOR("아웃도어", CategoryEnum.ACTIVITY),

    // Love
    DATE("데이트", CategoryEnum.LOVE),
    COUPLE("커플", CategoryEnum.LOVE),
    MIND("심리", CategoryEnum.LOVE),
    MARRIAGE("결혼", CategoryEnum.LOVE),
    BROKEUP("이별", CategoryEnum.LOVE),
    SOMETHING("썸", CategoryEnum.LOVE),
    LOVESTYLE("러브스타일", CategoryEnum.LOVE),
    COUNSEL("연애상담", CategoryEnum.LOVE),
    BLINDDATE("소개팅", CategoryEnum.LOVE),

    // SelfDev
    STUDY("스터디", CategoryEnum.SELFDEV),
    BOOK("독서", CategoryEnum.SELFDEV),
    BRANDING("브랜딩", CategoryEnum.SELFDEV),
    LANGUAGE("외국어", CategoryEnum.SELFDEV),
    DISCUSS("토론", CategoryEnum.SELFDEV),
    STOCK("주식", CategoryEnum.SELFDEV),
    ESTATE("부동산", CategoryEnum.SELFDEV),
    CAREER("커리어", CategoryEnum.SELFDEV),
    CHALLENGE("챌린지", CategoryEnum.SELFDEV),

    //HOBBY
    MAGIC("마술", CategoryEnum.HOBBY),
    STANDUP("스탠드업", CategoryEnum.HOBBY),
    COOK("요리", CategoryEnum.HOBBY),
    GARDENING("식물가꾸기", CategoryEnum.HOBBY),
    DIY("DIY", CategoryEnum.HOBBY),

    GAME("게임", CategoryEnum.HOBBY),
    ESCAPE("방탈출", CategoryEnum.HOBBY),
    BOARDGAME("보드게임", CategoryEnum.HOBBY),
    MUSICINST("악기", CategoryEnum.HOBBY),
    CAR("자동차", CategoryEnum.HOBBY),
    MOTORCYCLE("오토바이", CategoryEnum.HOBBY),
    BAKING("베이킹", CategoryEnum.HOBBY),
    WINE("와인", CategoryEnum.HOBBY),
    PLACE("맛집탐방", CategoryEnum.HOBBY),
    CAFE("카페탐방", CategoryEnum.HOBBY),
    PET("반려동물", CategoryEnum.HOBBY),
    PROGRAMMING("개발", CategoryEnum.HOBBY);


    private final String tag;
    private final CategoryEnum categoryEnum;

    TagEnum(String tag, CategoryEnum categoryEnum) {
        this.tag = tag;
        this.categoryEnum = categoryEnum;
    }

    public String getTag() {
        return this.tag;
    }

    public CategoryEnum getCategoryEnum() {
        return this.categoryEnum;
    }

    public static TagEnum fromString(String tag) {
        for (TagEnum tagEnum : TagEnum.values()) {
            if (tagEnum.getTag().equalsIgnoreCase(tag)) {
                return tagEnum;
            }
        }
        throw new IllegalArgumentException(String.format("No Tag was found for %s", tag));
    }

    public static List<String> parseTag(String tagString) {
        if (tagString == null) {
            return null;
        }
        List<String> tagList = new ArrayList<>();
        TagEnum[] tagValues = TagEnum.values();
        int i = 0;
        while (i <= tagString.length() - 1) {
            int idx = tagString.indexOf('1', i);
            if (idx == -1) {
                break;
            } else {
                tagList.add(tagValues[idx].getTag());
                i = idx + 1;
            }
        }
        return tagList;
    }

    public static String tagListToTagString(List<String> tagList) {
        if (tagList == null){
            tagList = new ArrayList<>();
        }
        List<TagEnum> tagEnumList = tagListToEnumList(tagList);
        String newString = "0".repeat(TagEnum.values().length);

        StringBuilder sb = new StringBuilder(newString);
        for (TagEnum tagEnum : tagEnumList) {
            sb.setCharAt(tagEnum.ordinal(), '1');
        }

        return sb.toString();
    }

    private static List<TagEnum> tagListToEnumList(List<String> tagList) {
        return tagList.stream()
                .map(TagEnum::fromString)
                .sorted()
                .toList();
    }

    public static List<TagEnum> tagEnumListOfCategory(CategoryEnum categoryEnum) {
        List<TagEnum> tagEnumList = new ArrayList<>();
        for (TagEnum tagEnum : TagEnum.values()) {
            if(tagEnum.getCategoryEnum() == categoryEnum) {
                tagEnumList.add(tagEnum);
            }
        }
        return tagEnumList;
    }
    public static int calculateSimilarity(String tagString1, String tagString2) {
        int count = 0;
        for (int i = 0; i < tagString1.length(); i++) {
            if (tagString1.charAt(i) == '1' && tagString2.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    public static List<Integer> tagIndexList(String tagString){
        if (tagString == null){return null;}
        int i = 0;
        List<Integer> tagIdxList = new ArrayList<>();
        while (i <= tagString.length() - 1) {
            int idx = tagString.indexOf('1', i);
            if (idx == -1) {
                break;
            } else {
                tagIdxList.add(i);
                i = idx + 1;
            }
        }
        return tagIdxList;
    }
}
