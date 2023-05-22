package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.user.entity.User;

import java.util.Calendar;

public class ClubResponseDto {
    private Long id;
    private User ownerId;
    private String title;
    private CategoryEnum category;
    private String content;
    private Calendar allowBirthBefore;
    private GenderEnum allowGender;
    private boolean requireApproval;
    private Integer maxGroupSize;
}
