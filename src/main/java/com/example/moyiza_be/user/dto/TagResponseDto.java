package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class TagResponseDto {
    private List<String> tags;

    public TagResponseDto(List<TagEnum> tagEnumList){
        this.tags = tagEnumList.stream().map(TagEnum::getTag).toList();
    }
}
