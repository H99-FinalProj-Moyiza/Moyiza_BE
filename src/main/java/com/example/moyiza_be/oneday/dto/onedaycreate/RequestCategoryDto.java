package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.CategoryEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestCategoryDto {
    private String oneDayCategory;

    public RequestCategoryDto(String category) {
        this.oneDayCategory = category;
    }

    public CategoryEnum getCategoryEnum(){
        return CategoryEnum.fromString(this.oneDayCategory);
    }
}
