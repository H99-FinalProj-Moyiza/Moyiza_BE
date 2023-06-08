package com.example.moyiza_be.common.enums;

import jdk.jfr.Category;
import org.hibernate.engine.jdbc.BinaryStream;

import java.util.ArrayList;
import java.util.List;

public enum TagEnum {
    //스포츠
    SOCCER("축구", CategoryEnum.SPORTS),
    BASEBALL("야구", CategoryEnum.SPORTS),
    BADMINTON("배드민턴", CategoryEnum.SPORTS),
    CLIMBING("클라이밍", CategoryEnum.SPORTS),

    //운동
    WALKING("산책", CategoryEnum.FITNESS),
    GYM("헬스", CategoryEnum.FITNESS),
    YOGA("요가", CategoryEnum.FITNESS),
    PILATES("필라테스", CategoryEnum.FITNESS),

    //액티비티
    CAMPING("캠핑", CategoryEnum.ACTIVITY),
    HIKING("등산", CategoryEnum.ACTIVITY),
    FISHING("낚시", CategoryEnum.ACTIVITY),
    SNOWBOARDING("스노우보드", CategoryEnum.ACTIVITY),


    //여행
    DOMESTIC("국내여행", CategoryEnum.TRAVEL),
    OVERSEAS("해외여행", CategoryEnum.TRAVEL),
    ADVENTURE("탐험", CategoryEnum.TRAVEL),

    //문화
    MUSIC("음악", CategoryEnum.CULTURE),
    MOVIE("영화", CategoryEnum.CULTURE),
    BOOK("독서", CategoryEnum.CULTURE),

    //예술
    ARTS("공연예술", CategoryEnum.ART),
    VISUALARTS("시각예술", CategoryEnum.ART),

    //FOOD - ?
    CAFE("카페투어", CategoryEnum.FOOD),
    MICHELIN("맛집투어", CategoryEnum.FOOD),

    //SELFDEV
    STUDY("스터디", CategoryEnum.SELFDEV),
    PROJECT("프로젝트", CategoryEnum.SELFDEV),
    SEMINAR("세미나", CategoryEnum.SELFDEV),
    CHALLENGE("챌린지", CategoryEnum.SELFDEV),

    //HOBBY
    MAGIC("마술", CategoryEnum.HOBBY),
    STANDUP("스탠드업", CategoryEnum.HOBBY),
    ONLINEGAME("온라인게임", CategoryEnum.HOBBY),
    CONSOLEGAME("콘솔게임", CategoryEnum.HOBBY),
    PROGRAMMING("개발", CategoryEnum.HOBBY),


    //DATE - ??
    LOVERELATED("연애관련?", CategoryEnum.DATE);



    private final String tag;
    private final CategoryEnum categoryEnum;

    TagEnum(String tag, CategoryEnum categoryEnum) {
        this.tag = tag;
        this.categoryEnum = categoryEnum;
    }

    public String getTag(){return this.tag;}
    public CategoryEnum getCategoryEnum(){return this.categoryEnum;}

    public static TagEnum fromString(String tag) {
        for (TagEnum tagEnum : TagEnum.values()) {
            if (tagEnum.getTag().equalsIgnoreCase(tag)) {
                return tagEnum;
            }
        }
        throw new IllegalArgumentException(String.format("%s에 해당하는 Tag를 찾을 수 없습니다",tag));
    }

    public static List<String> parseTag(String tagString){
        if(tagString == null){return null;}
        List<String> tagList= new ArrayList<>();
        TagEnum[] tagValues = TagEnum.values();
        int i = 0;
        while(i <= tagString.length() -1){
             int idx = tagString.indexOf('1',i);
             if(idx == -1){break;}
             else{
                 tagList.add(tagValues[idx].getTag());
                 i = idx+1;
             }
        }
        return tagList;
    }
}
