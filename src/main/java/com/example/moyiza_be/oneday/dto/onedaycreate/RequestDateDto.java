package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestDateDto {
    @DateTimeFormat(pattern = "YYYY-MM-dd'T'HH:mm")
    private LocalDateTime dateTime;
}
