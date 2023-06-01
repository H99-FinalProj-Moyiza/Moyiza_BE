package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.oneday.dto.OneDayRequestDto;
import com.example.moyiza_be.oneday.dto.OneDayUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;

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
    private Calendar oneDayStartTime;
    @Column
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

    public OneDay(OneDayRequestDto requestDto, Long userId, String storedFileUrl) {
        this.ownerId = userId;
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
        this.oneDayImage = storedFileUrl;
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
