package com.example.moyiza_be.club.dto.createclub;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Getter
@NoArgsConstructor
public class CreateRequestPolicyDto {
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
}
