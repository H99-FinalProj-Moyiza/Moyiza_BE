package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OneDayDetailResponseDto {
    private long id;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private String genderPolicy;
    private Integer agePolicy;
    private String category;
    private List<String> tagString;
    //    private Tag tag;
    private LocalDateTime oneDayStartTime;
    private int oneDayGroupSize;
    private boolean deleted;
    private List<String> imageList;
    private List<OneDayAttendant> oneDayAttendantList;
    private int oneDayAttendantListSize;
    private String type;

    public OneDayDetailResponseDto(OneDay oneDay,List<String> oneDayImageUrlList, List<OneDayAttendant> attendantList, Integer people) {
        this.id = oneDay.getId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.category = oneDay.getCategory().getCategory();
        this.tagString = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.genderPolicy = oneDay.getGenderPolicy().getGenderPolicy();
        this.agePolicy = oneDay.getAgePolicy();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.deleted = oneDay.getDeleted();
        this.type = oneDay.getType().getOneDayType();
        this.imageList = oneDayImageUrlList;
        this.oneDayAttendantList = attendantList;
        this.oneDayAttendantListSize = people;
    }
}
