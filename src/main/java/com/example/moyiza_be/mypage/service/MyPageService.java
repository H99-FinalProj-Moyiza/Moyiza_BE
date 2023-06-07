package com.example.moyiza_be.mypage.service;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.ClubJoinEntry;
import com.example.moyiza_be.club.repository.ClubJoinEntryRepository;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.mypage.Dto.ClubResponseDto;
import com.example.moyiza_be.mypage.Dto.PageResponseDto;
import com.example.moyiza_be.mypage.Dto.UserResponseDto;
import com.example.moyiza_be.mypage.repository.MyPageRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final ClubRepository clubRepository;
    private final ClubJoinEntryRepository clubJoinEntryRepository;
    @Transactional
    public PageResponseDto getMyPage(User user) {
        UserResponseDto userInfo = UserResponseDto.builder()
                .user_id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .clubsInOperationCount(clubRepository.findByOwnerId(user.getId()).size())
                .clubsInParticipatingCount(clubJoinEntryRepository.findByUserId(user.getId()).size())
                .build();

        //운영중인 클럽 정보 리스트
        List<Club> clubsInOperation = clubRepository.findByOwnerId(user.getId());
        List<ClubResponseDto> clubsInOperationInfo = clubsInOperation.stream()
                .map(club -> ClubResponseDto.builder()
                        .club_id(club.getId())
                        .clubCategory(club.getCategory().getCategory())
                        .clubTag(club.getTagString())
                        .clubTitle(club.getTitle())
                        .thumbnailUrl(club.getThumbnailUrl())
                        .nowMemberCount(club.getNowMemberCount())
                                .build())
                .collect(Collectors.toList());

        // 참여중인 클럽 정보 리스트
        List<ClubJoinEntry> clubsInParticipatingEntry = clubJoinEntryRepository.findByUserId(user.getId());
        List<Long> clubIds = clubsInParticipatingEntry.stream()
                .map(ClubJoinEntry::getClubId)
                .collect(Collectors.toList());

        List<Club> clubsInParticipating = clubRepository.findAllById(clubIds);
        List<ClubResponseDto> clubsInParticipatingInfo = clubsInParticipating.stream()
                .map(club -> ClubResponseDto.builder()
                        .club_id(club.getId())
                        .clubCategory(club.getCategory().getCategory())
                        .clubTag(club.getTagString())
                        .clubTitle(club.getTitle())
                        .thumbnailUrl(club.getThumbnailUrl())
                        .nowMemberCount(club.getNowMemberCount())
                        .build())
                .collect(Collectors.toList());

                return new PageResponseDto(userInfo, clubsInOperationInfo, clubsInParticipatingInfo);
    }
}
