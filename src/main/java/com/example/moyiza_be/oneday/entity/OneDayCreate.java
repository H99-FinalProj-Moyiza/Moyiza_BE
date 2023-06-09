package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    private double oneDayLatitude;
    private double oneDayLongitude;
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'HH:mm")
    private LocalDateTime oneDayStartTime;
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    private Integer oneDayGroupSize;
    private Boolean deleted;
    private Integer attendantsNum;
    private String oneDayImage;
    private Boolean confirmed = false;
    private OneDayTypeEnum oneDayType;

    public OneDayCreate(Long ownerId) {
        this.ownerId = ownerId;
    }
}


