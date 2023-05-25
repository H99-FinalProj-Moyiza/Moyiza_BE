package com.example.moyiza_be.club.dto.createclub;

import com.example.moyiza_be.common.enums.CategoryEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRequestCategoryDto {
    private String category;

    public CreateRequestCategoryDto(String category) {
        this.category = category;
    }

    public CategoryEnum getCategoryEnum(){
        return CategoryEnum.fromString(this.category);
    }
}
