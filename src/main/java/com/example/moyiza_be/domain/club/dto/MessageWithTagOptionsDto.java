package com.example.moyiza_be.domain.club.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.Message;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class MessageWithTagOptionsDto extends Message {
    private final List<String> tagOptions;

    public MessageWithTagOptionsDto(String message, CategoryEnum categoryEnum) {
        super(message);
        this.tagOptions = Arrays.stream(TagEnum.values()).map(TagEnum::getTag).toList();
    }
}
