package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.user.entity.User;

import java.util.Calendar;

public class ClubResponseDto {
    private Long id;
    private User ownerId;
    private String title;
    private CategoryEnum category;
    private TagEnum tag;
    private String content;
    private Calendar allowBirthBefore;
    private GenderEnum allowGender;
    private boolean requireApproval;
    private Integer maxGroupSize;

    public ClubResponseDto(Club club) {
        this.id = club.getId();
        this.ownerId = club.getOwnerId();
        this.title = club.getTitle();
        this.category = club.getCategory();
        this.tag = club.getTag();
        this.content = club.getContent();
        this.allowBirthBefore = club.getAllowBirthBefore();
        this.allowGender = club.getAllowGender();
        this.requireApproval = club.getRequireApproval();
        this.maxGroupSize = club.getMaxGroupSize();
    }
}
