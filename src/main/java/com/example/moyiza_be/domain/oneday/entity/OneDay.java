package com.example.moyiza_be.domain.oneday.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.domain.oneday.dto.OneDayUpdateRequestDto;
import com.example.moyiza_be.domain.oneday.dto.onedaycreate.OneDayCreateConfirmDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OneDay extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "oneDayId")
    private Long id;

    @Column(nullable = false)
    private Long ownerId;
    @Column(nullable = false)
    private String oneDayTitle;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String oneDayContent;
    @Column
    private CategoryEnum category;
    @Column
    private String tagString;
    @Column
    private String oneDayLocation;
    @Column
    private double oneDayLatitude;
    @Column
    private double oneDayLongitude;
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
    private Boolean deleted = false;
    @Column
    private Integer attendantsNum = 0;
    @Column(name = "image_url")
    @Lob
    private String  oneDayImage;
    @Column
    @Enumerated(EnumType.STRING)
//    private OneDayTypeEnum type = OneDayTypeEnum.FCFSB;
    private OneDayTypeEnum type = OneDayTypeEnum.APPROVAL;

    @Column
    private Integer numLikes = 0;

//    @Column
//    private boolean expired = false;

    public OneDay(OneDayCreateConfirmDto requestDto) {
        this.ownerId = requestDto.getOwnerId();
        this.oneDayImage = requestDto.getOneDayImage();
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
        this.type = requestDto.getOneDayType;
    }

    public void addAttendantNum(){
        attendantsNum++;
    }
    public void minusAttendantNum(){
        attendantsNum--;
    }

    public void addLike(){
        if(this.numLikes == null){this.numLikes = 0;}
        this.numLikes++;
    }
    public void minusLike(){
        this.numLikes--;
    }

    public void updateAll(OneDayUpdateRequestDto requestDto) {
        this.oneDayTitle = requestDto.getOneDayTitle();
        this.oneDayContent = requestDto.getOneDayContent();
        this.category = CategoryEnum.fromString(requestDto.getCategory());
        List<TagEnum> tagEnumList = requestDto.getTagString().stream().map(TagEnum::fromString).toList();
        String newString = "0".repeat(TagEnum.values().length);

        StringBuilder sb = new StringBuilder(newString);
        for (TagEnum tagEnum : tagEnumList) {
            sb.setCharAt(tagEnum.ordinal(), '1');
        }
        this.tagString = sb.toString();
        this.agePolicy = requestDto.getAgePolicy();
        this.genderPolicy = GenderPolicyEnum.fromString(requestDto.getGenderPolicy());
        this.oneDayLocation = requestDto.getOneDayLocation();
        this.oneDayLatitude = requestDto.getOneDayLatitude();
        this.oneDayLongitude = requestDto.getOneDayLongitude();
        this.oneDayGroupSize = requestDto.getOneDayGroupSize();
        this.oneDayStartTime = requestDto.getOneDayStartTime();
        this.type = OneDayTypeEnum.fromString(requestDto.getType());
    }

    public void updateOneDayImage(String storedFileUrl) {
        this.oneDayImage = storedFileUrl;
    }

}
