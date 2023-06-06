package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.oneday.dto.onedaycreate.OneDayCreateConfirmDto;
import com.example.moyiza_be.oneday.dto.OneDayUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OneDay extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oneDayId")
    private Long id;

    @Column(nullable = false)
    private Long ownerId;
    @Column(nullable = false)
    private String oneDayTitle;
    @Column(nullable = false)
    private String oneDayContent;
    @Column
    private CategoryEnum category;
    @Column
    private String tagString;
    @Column
    private String oneDayLocation;
    @Column
    private String oneDayLatitude;
    @Column
    private String oneDayLongitude;
    @Column
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'HH:mm")
    private LocalDateTime oneDayStartTime;
    @Column
    @Enumerated(EnumType.STRING)
    private GenderPolicyEnum genderPolicy;
    @Column
    private Integer agePolicy;
    @Column
    private Integer oneDayGroupSize;
    @Column
    private Boolean deleted;
    @Column
    private Integer attendantsNum;
    @Column(name = "image_url")
    @Lob
    private String  oneDayImage;
    @Column
    @Enumerated(EnumType.STRING)
    private OneDayTypeEnum type;

    public OneDay(OneDayCreateConfirmDto requestDto) {
        this.ownerId = requestDto.getOwnerId();
        this.oneDayTitle = requestDto.getOneDayTitle();
        this.oneDayContent = requestDto.getOneDayContent();
        this.category = requestDto.getCategory();
        this.tagString = requestDto.getTagString();
        this.agePolicy = requestDto.getAgePolicy();
        this.genderPolicy = requestDto.getGenderPolicy();
        this.oneDayLocation = requestDto.getOneDayLocation();
        this.oneDayLatitude = requestDto.getOneDayLatitude();
        this.oneDayLongitude = requestDto.getOneDayLongitude();
        this.oneDayGroupSize = requestDto.getOneDayGroupSize();
        this.oneDayStartTime = requestDto.getOneDayStartTime();
        this.oneDayImage = requestDto.getOneDayImage();
        this.type = requestDto.getGetOneDayType();
    }

    public void oneDayAttend(){
        attendantsNum++;
    }

    public void oneDayCancel(){
        attendantsNum--;
    }

    public void updateOneDay(OneDayUpdateRequestDto requestDto) {
        this.oneDayTitle = requestDto.getOneDayTitle();
        this.oneDayContent = requestDto.getOneDayContent();
        this.category = requestDto.getCategory();
        this.tagString = requestDto.getTagString();
        this.agePolicy = requestDto.getAgePolicy();
        this.genderPolicy = requestDto.getGenderPolicy();
        this.oneDayLocation = requestDto.getOneDayLocation();
        this.oneDayLatitude = requestDto.getOneDayLatitude();
        this.oneDayLongitude = requestDto.getOneDayLongitude();
        this.oneDayGroupSize = requestDto.getOneDayGroupSize();
        this.oneDayStartTime = requestDto.getOneDayStartTime();
    }

    public void updateOneDayImage(String storedFileUrl) {
        this.oneDayImage = storedFileUrl;
    }

}
