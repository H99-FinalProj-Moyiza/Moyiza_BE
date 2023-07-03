package com.example.moyiza_be.user.dto;

import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.oneday.dto.OneDayListResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class LikeResponseDto {
    private Page<ClubListResponse> likeClubList;
    private Page<OneDayListResponseDto> likeOneDayList;

    public LikeResponseDto(Page<ClubListResponse> likeClubList, Page<OneDayListResponseDto> likeOneDayList) {
        this.likeClubList = likeClubList;
        this.likeOneDayList = likeOneDayList;
    }
}
