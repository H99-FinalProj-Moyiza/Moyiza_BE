package com.example.moyiza_be.oneday.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.oneday.dto.OneDayRequestDto;
import com.example.moyiza_be.oneday.dto.OneDayUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OneDay extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ownerId;
    @Column(nullable = false)
    private String oneDayTitle;
    @Column(nullable = false)
    private String oneDayContent;
    @Column
    private String oneDayLocation;
    @Column
    private String oneDayLatitude;
    @Column
    private String oneDayLongitude;
    @Column
    private Calendar oneDayStartTime;
    @Column
    private int oneDayGroupSize;
    @Column
    private boolean deleted;
    @Column
    private int attendantsNum;

    public OneDay(OneDayRequestDto requestDto, Long userId) {
        this.ownerId = userId;
        this.oneDayTitle = requestDto.getOneDayTitle();
        this.oneDayContent = requestDto.getOneDayContent();
        this.oneDayLocation = requestDto.getOneDayLocation();
        this.oneDayLatitude = requestDto.getOneDayLatitude();
        this.oneDayLongitude = requestDto.getOneDayLongitude();
        this.oneDayGroupSize = requestDto.getOneDayGroupSize();
        this.oneDayStartTime = requestDto.getOneDayStartTime();
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
        this.oneDayLocation = requestDto.getOneDayLocation();
        this.oneDayLatitude = requestDto.getOneDayLatitude();
        this.oneDayLongitude = requestDto.getOneDayLongitude();
        this.oneDayGroupSize = requestDto.getOneDayGroupSize();
        this.oneDayStartTime = requestDto.getOneDayStartTime();
    }

}
