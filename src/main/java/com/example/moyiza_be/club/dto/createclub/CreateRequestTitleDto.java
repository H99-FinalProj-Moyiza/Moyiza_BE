package com.example.moyiza_be.club.dto.createclub;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRequestTitleDto {
    private String title;

    public CreateRequestTitleDto(String title) {
        this.title = title;
    }
}
