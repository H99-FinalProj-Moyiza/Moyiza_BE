package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class EnumOptionsDto {
    private final List<String> categoryList = Arrays.stream(CategoryEnum.values()).map(CategoryEnum::getCategory).toList();
//    private final List<String> tagList = Arrays.stream(TagEnum.values()).map(TagEnum::getTag).toList();
    private final List<String> genderPolicyList = Arrays.stream(GenderPolicyEnum.values()).map(GenderPolicyEnum::getGenderPolicy).toList();
}
