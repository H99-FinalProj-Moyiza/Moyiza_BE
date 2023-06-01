package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OneDayCreate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ownerId;
    private String oneDayTitle;
    private String oneDayContent;
    private CategoryEnum category;
    private String tagString;
    private String oneDayLocation;
    private String oneDayLatitude;
    private String oneDayLongitude;
    private Calendar oneDayStartTime;
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    private Integer oneDayGroupSize;
    private Boolean deleted;
    private Integer attendantsNum;
    private String  oneDayImage;
    private Boolean confirmed = false;

}
