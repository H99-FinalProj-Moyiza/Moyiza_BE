package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OneDayDetailResponseDto {
    private final long id;
    private final String oneDayTitle;
    private final String oneDayContent;
    private final String oneDayLocation;
    private final double oneDayLatitude;
    private final double oneDayLongitude;
    private final String genderPolicy;
    private final Integer agePolicy;
    private final String category;
    private final List<String> tagString;
    //    private Tag tag;
    private final LocalDateTime oneDayStartTime;
    private final int oneDayGroupSize;
    private final List<String> imageList;
    private final List<OneDayAttendant> oneDayAttendantList;
    private final int oneDayAttendantListSize;
    private final String type;

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
        this.type = oneDay.getType().getOneDayType();
        this.imageList = oneDayImageUrlList;
        this.oneDayAttendantList = attendantList;
        this.oneDayAttendantListSize = people;
    }
}
