package com.example.moyiza_be.domain.user.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateRequestDto {
    private String nickname;
    private List<String> tags;
    private String imageUrl;
    private String content;

    public List<TagEnum> getTagEnumList() {
        return tags.stream()
                .map(TagEnum::fromString)
                .sorted()
                .toList();
    }
}
