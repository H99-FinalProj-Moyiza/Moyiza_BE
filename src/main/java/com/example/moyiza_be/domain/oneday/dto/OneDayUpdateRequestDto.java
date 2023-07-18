package com.example.moyiza_be.domain.oneday.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OneDayUpdateRequestDto {
    private String oneDayTitle;
    private String oneDayContent;
    private String category;
    private List<String> tagString;
    private String genderPolicy;
    private Integer agePolicy;
    private String type;
    private LocalDateTime oneDayStartTime;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private Integer oneDayGroupSize;
}
