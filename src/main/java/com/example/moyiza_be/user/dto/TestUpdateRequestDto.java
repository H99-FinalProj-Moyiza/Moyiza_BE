package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class TestUpdateRequestDto {
    private String nickname;
    private List<String> tags;
    private String imageUrl;

    public List<TagEnum> getTagEnumList() {
        return tags.stream()
                .map(TagEnum::fromString)
                .sorted()
                .toList();
    }
}
