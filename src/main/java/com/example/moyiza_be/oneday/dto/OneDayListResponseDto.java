package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class OneDayListResponseDto {
    private final Long onedayId;
    private final String ownerNickname;
    private final String onedayTitle;
    private final List<String> onedayTag;
    private final Integer onedayGroupSize;
    private final Integer onedayAttendantsNum;
    private final String thumbnailUrl;
    private final Double longitude;
    private final Double latitude;
    private final String onedayLocation;

    @QueryProjection
    public OneDayListResponseDto(
            Long onedayId, String ownerNickname, String onedayTitle, String tagString, Integer onedayGroupSize,
            Integer onedayAttendantsNum, String thumbnailUrl, Double longitude, Double latitude, String onedayLocation
    ) {
        this.onedayId = onedayId;
        this.ownerNickname = ownerNickname;
        this.onedayTitle = onedayTitle;
        this.onedayTag = TagEnum.parseTag(tagString);
        this.onedayGroupSize = onedayGroupSize;
        this.onedayAttendantsNum = onedayAttendantsNum;
        this.thumbnailUrl = thumbnailUrl;
        this.longitude = longitude;
        this.latitude = latitude;
        this.onedayLocation = onedayLocation;
    }
}
