package com.example.moyiza_be.club.service;

import com.example.moyiza_be.club.dto.ClubMemberDto;
import com.example.moyiza_be.club.dto.ClubResponseDto;
import com.example.moyiza_be.club.dto.ConfirmClubCreationDto;
import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.ClubJoinEntry;
import com.example.moyiza_be.club.repository.ClubJoinEntryRepository;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubJoinEntryRepository clubJoinEntryRepository;
    private final UserRepository userRepository;

    //클럽 가입
    public ResponseEntity<Message> joinClub(Long clubId, User user) {
        ClubJoinEntry joinEntry = new ClubJoinEntry(user.getId(), clubId);
        clubJoinEntryRepository.save(joinEntry);
        Message message = new Message("가입이 승인되었습니다.");
        return ResponseEntity.ok(message);
    }

    //클럽 전체 조회
    public ResponseEntity<List<ClubResponseDto>> getClubList() {
        List<Club> clubList = clubRepository.findAll();
        List<ClubResponseDto> responseDtoList = clubList.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtoList);
    }

    //클럽 검색 조회
    public ResponseEntity<List<ClubResponseDto>> searchClubList(String category, String q) {
        List<Club> clubList;
        if (category != null && q != null) {
            //카테고리와 검색어를 모두 입력한 경우
            clubList = clubRepository.findByCategoryAndTitleContaining(category, q);
        } else if (category != null) {
            //카테고리만 입력한 경우
            clubList = clubRepository.findByCategory(category);
        } else if (q != null) {
            clubList = clubRepository.findByTitleContaining(q);
        } else {
            //카테고리와 검색어가 모두 입력되지 않은 경우 전체 클럽 조회
            clubList = clubRepository.findAll();
        }

        List<ClubResponseDto> responseDtoList = clubList.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtoList);
    }


    //클럽 상세 조회
    public ResponseEntity<ClubResponseDto> getClub(Long clubId) {
        Club club = checkNullClub(clubId);
        ClubResponseDto responseDto = new ClubResponseDto(club);
        return ResponseEntity.ok(responseDto);
    }


    //클럽 멤버 조회
    public ResponseEntity<List<ClubMemberDto>> getClubMember(Long clubId) {
        checkNullClub(clubId);
        List<ClubJoinEntry> joinEntryList = clubJoinEntryRepository.findByClubId(clubId);

        List<ClubMemberDto> memberDtoList = joinEntryList.stream()
                .map(joinEntry -> userRepository.findById(joinEntry.getUserId())
                        .map(user -> new ClubMemberDto(joinEntry.getUserId(), user.getNickname(), user.getEmail()))
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDtoList);
    }

    //클럽 탈퇴
    public ResponseEntity<Message> goodbyeClub(Long clubId, User user) {
        checkNullClub(clubId);

        ClubJoinEntry joinEntry = clubJoinEntryRepository.findByUserIdAndClubId(user.getId(), clubId);
        if (joinEntry != null) {
            clubJoinEntryRepository.delete(joinEntry);
            Message message = new Message("클럽에서 탈퇴되었습니다.");
            return ResponseEntity.ok(message);
        } else {
            Message message = new Message("클럽 가입 정보가 없습니다.");
            return ResponseEntity.ok(message);
        }
    }

    //클럽 강퇴
    public ResponseEntity<Message> banclub(Long clubId, User user) {
        checkNullClub(clubId);

        ClubJoinEntry joinEntry = clubJoinEntryRepository.findByUserIdAndClubId(user.getId(), clubId);
        if (joinEntry != null) {
            clubJoinEntryRepository.delete(joinEntry);
            Message message = new Message("클럽에서 강퇴되었습니다.");
            return ResponseEntity.ok(message);
        } else {
            Message message = new Message("클럽 가입 정보가 없습니다.");
            return ResponseEntity.ok(message);
        }
    }

    //클럽 생성
    public ClubResponseDto createClub(ConfirmClubCreationDto creationRequest){
        Club club = new Club(creationRequest);
        clubRepository.saveAndFlush(club);
        return new ClubResponseDto(club);
    }

    ///////////////////////private method///////////////////////

    //클럽id Null 체크
    private Club checkNullClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new NullPointerException()
        );
        return club;
    }
}
