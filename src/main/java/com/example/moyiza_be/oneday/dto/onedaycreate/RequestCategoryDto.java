package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.CategoryEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCategoryDto {
    private String category;

    public RequestCategoryDto(String category) {
        this.category = category;
    }

    public CategoryEnum getCategoryEnum(){
        return CategoryEnum.fromString(this.category);
    }
}
