package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestTagDto {
    private List<String> tag;


    public List<TagEnum> getTagEnumList() {
        return tag.stream()
                .map(TagEnum::fromString)
                .sorted()
                .toList();
    }
}