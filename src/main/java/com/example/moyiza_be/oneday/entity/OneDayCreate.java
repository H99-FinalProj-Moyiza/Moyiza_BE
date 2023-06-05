package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

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
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'HH:mm")
    private LocalDateTime oneDayStartTime= LocalDateTime.now();
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    private Integer oneDayGroupSize = 3;
    private Boolean deleted;
    private Integer attendantsNum;
    private String oneDayImage;
    private Boolean confirmed = false;
    private OneDayTypeEnum oneDayType;
}
