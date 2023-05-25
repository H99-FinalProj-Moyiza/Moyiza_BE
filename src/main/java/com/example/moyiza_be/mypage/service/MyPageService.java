package com.example.moyiza_be.mypage.service;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.mypage.Dto.PageResponseDto;
import com.example.moyiza_be.mypage.repository.MyPageRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepository myPageRepository;
    @Transactional
    public PageResponseDto getMyPage(User user) {
        List<Club> clubList = myPageRepository.findAllByOwnerId(user.getId());
        return new PageResponseDto(user, clubList);
    }
}
