package com.example.moyiza_be.club.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RemoveImageRequest {
    private List<String> deleteImage;

    public RemoveImageRequest(List<String> deleteImage) {
        this.deleteImage = deleteImage;
    }
}
