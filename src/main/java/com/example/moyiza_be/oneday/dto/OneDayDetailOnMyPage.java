package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OneDayDetailOnMyPage {
    private long oneDayId;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private String category;
    private List<String> tagString;
    private int oneDayGroupSize;
    private String oneDayImage;
    private int oneDayAttendantListSize;
    private Integer oneDayNumLikes;
    private Boolean oneDayIsLikedByUser;

    public OneDayDetailOnMyPage(OneDay oneDay) {
        this.oneDayId = oneDay.getId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.category = oneDay.getCategory().getCategory();
        this.tagString = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.oneDayImage = oneDay.getOneDayImage();
        this.oneDayNumLikes = oneDay.getNumLikes();
    }
}
