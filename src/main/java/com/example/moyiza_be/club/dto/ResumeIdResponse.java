package com.example.moyiza_be.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ResumeIdResponse {
    private final Long createclub_id;

    public ResumeIdResponse(Long createclub_id) {
        this.createclub_id = createclub_id;
    }
}
