package com.example.moyiza_be.alert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AlertResponseDto {
    private Long id;
    private String message;
    private String receiver;
    private boolean check;
}
