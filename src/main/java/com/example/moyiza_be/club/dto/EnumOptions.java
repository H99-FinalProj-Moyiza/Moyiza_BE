package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import jdk.jfr.Category;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Getter
public class EnumOptions {
    private final List<String> categoryList;
    private final Map<String, List<String>> categoryAndTagList;
    private final List<String> genderPolicyList;

    public EnumOptions() {
        this.categoryList = Arrays.stream(CategoryEnum.values())
                .map(CategoryEnum::getCategory)
                .toList();
        this.categoryAndTagList = Arrays.stream(TagEnum.values())
                .collect(
                        Collectors.groupingBy(
                                tagEnum -> tagEnum.getCategoryEnum().getCategory(), mapping(TagEnum::getTag,toList())
                        )
                );
        //    private final List<String> tagList = Arrays.stream(TagEnum.values()).map(TagEnum::getTag).toList();
        this.genderPolicyList = Arrays.stream(GenderPolicyEnum.values()).map(GenderPolicyEnum::getGenderPolicy).toList();
    }
}
