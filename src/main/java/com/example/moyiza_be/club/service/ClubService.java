package com.example.moyiza_be.club.service;

import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.club.dto.*;
import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.ClubImageUrl;
import com.example.moyiza_be.club.entity.ClubJoinEntry;
import com.example.moyiza_be.club.repository.ClubImageUrlRepository;
import com.example.moyiza_be.club.repository.ClubJoinEntryRepository;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.club.repository.QueryDSL.ClubImageUrlRepositoryCustom;
import com.example.moyiza_be.club.repository.QueryDSL.ClubJoinEntryRepositoryCustom;
import com.example.moyiza_be.club.repository.QueryDSL.ClubRepositoryCustom;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.event.dto.EventSimpleDetailDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.service.EventService;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubJoinEntryRepository clubJoinEntryRepository;
    private final EventService eventService;
    private final ClubImageUrlRepository clubImageUrlRepository;
    private final ChatService chatService;
    private final ClubJoinEntryRepositoryCustom clubJoinEntryRepositoryCustom;
    private final ClubRepositoryCustom clubRepositoryCustom;
    private final ClubImageUrlRepositoryCustom clubImageUrlRepositoryCustom;


    //클럽 가입
    public ResponseEntity<Message> joinClub(Long clubId, User user) {
        Club club = loadClubByClubId(clubId);

        if (clubJoinEntryRepository.existsByClubIdAndUserId(clubId, user.getId())) {
            return new ResponseEntity<>(new Message("중복으로 가입할 수 없습니다"), HttpStatus.BAD_REQUEST);
        }

        club.addAttend();
        clubRepository.save(club);

        ClubJoinEntry joinEntry = new ClubJoinEntry(user.getId(), clubId);
        clubJoinEntryRepository.save(joinEntry);
        chatService.joinChat(clubId, ChatTypeEnum.CLUB, user);

        //미래에 조건검증 추가
        Message message = new Message("가입이 승인되었습니다.");
        return ResponseEntity.ok(message);
    }

    //클럽 리스트 조회(전체조회, 검색조회 포함)
    public ResponseEntity<Page<ClubListResponse>> getClubList(
            Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3
    ) {

        Page<ClubListResponse> responseList = clubRepositoryCustom.filteredClubResponseList(
                pageable, category, q, tag1, tag2, tag3);
        return ResponseEntity.ok(responseList);
    }

    //클럽 상세 조회
    public ResponseEntity<ClubDetailResponse> getClubDetail(Long clubId) {
        ClubDetailResponse clubDetailResponse = clubRepositoryCustom.getClubDetail(clubId);
        if(clubDetailResponse == null){
            throw new NullPointerException("클럽을 찾을 수 없습니다");
        }
        List<String> clubImageUrlList = clubImageUrlRepositoryCustom.getAllImageUrlByClubId(clubId);
        clubDetailResponse.setClubImageUrlList(clubImageUrlList);
        return ResponseEntity.ok(clubDetailResponse);
    }

    //마이페이지 클럽 리스트 조회
    public ClubListOnMyPage getClubListOnMyPage(Long userId) {
        List<ClubDetailResponse> clubsInOperationInfo = clubRepositoryCustom.getManagedClubDetail(userId);
        List<ClubDetailResponse> clubsInParticipatingInfo = clubRepositoryCustom.getJoinedClubDetail(userId);
        return new ClubListOnMyPage(clubsInOperationInfo, clubsInParticipatingInfo);
    }

    //클럽 멤버 조회
    //프로필사진, 닉네임, 클럽 가입 날짜
    public ResponseEntity<List<ClubMemberResponse>> getClubMember(Long clubId) {

        List<ClubMemberResponse> clubMemberResponseList = clubJoinEntryRepositoryCustom.getClubMemberList(clubId);

        return ResponseEntity.ok(clubMemberResponseList);
    }

    //클럽 탈퇴
    public ResponseEntity<Message> goodbyeClub(Long clubId, User user) {
        //가입회원수 조회 Query로 할 수 없는지 ? entity에 attend 숫자 없이
        Club club = loadClubByClubId(clubId);
        ClubJoinEntry clubJoinEntry = loadClubJoinEntry(user.getId(), clubId);

        clubJoinEntryRepository.delete(clubJoinEntry);
        club.cancelAttend();
        clubRepository.saveAndFlush(club);
        chatService.leaveChat(clubId, ChatTypeEnum.CLUB, user);
        Message message = new Message("클럽에서 탈퇴되었습니다.");
        return ResponseEntity.ok(message);
    }

    //클럽 강퇴
    public ResponseEntity<Message> banClub(Long clubId, User user, BanRequest banRequest) {
        if (!clubRepository.existsByIdAndIsDeletedFalseAndOwnerIdEquals(clubId, user.getId())) {
            throw new IllegalAccessError("권한이 없습니다");
        }
        Club club = loadClubByClubId(clubId);
        ClubJoinEntry joinEntry = loadClubJoinEntry(banRequest.getBanUserId(), clubId);

        clubJoinEntryRepository.delete(joinEntry);
        club.cancelAttend();
        clubRepository.saveAndFlush(club);
        chatService.leaveChat(clubId, ChatTypeEnum.CLUB, user);

        log.info("user " + user.getId() + " banned user " + banRequest.getBanUserId() + " from club " + clubId);

        //추방 후 가입 제한 추가시 여기에 logic
        Message message = new Message(String.format("user %d 가 클럽에서 강퇴되었습니다", banRequest.getBanUserId()));
        return ResponseEntity.ok(message);
    }

    //클럽 생성
    public ClubDetailResponse createClub(ConfirmClubCreationDto creationRequest, User user) {
        Club club = new Club(creationRequest);
        clubRepository.saveAndFlush(club);
        chatService.makeChat(club.getId(), ChatTypeEnum.CLUB, club.getTitle());
        joinClub(club.getId(), user);
        List<String> clubImageUrlList = clubImageUrlRepository.findAllByCreateClubId(creationRequest.getCreateClubId())
                .stream()
                .peek(clubImageUrl -> clubImageUrl.setClubId(club.getId()))
                .map(ClubImageUrl::getImageUrl)
                .toList();
        return new ClubDetailResponse(club, clubImageUrlList); // querydsl에서 List로 projection이 가능한가 확인해봐야함
    }

    public ResponseEntity<List<EventSimpleDetailDto>> getClubEventList(User user, Long clubId) {
        List<EventSimpleDetailDto> eventList = eventService.getEventList(clubId);
        return ResponseEntity.ok(eventList);
    }

    public ResponseEntity<Message> deleteClub(User user, Long clubId) {
        //임시구현, 로직 변경 필요할듯 (softdelete ? orphanremoval ?
        Club club = loadClubByClubId(clubId);
        if (!club.getOwnerId().equals(user.getId())) {
            return new ResponseEntity<>(new Message("내 클럽이 아닙니다"), HttpStatus.UNAUTHORIZED);
        } else {
            club.flagDeleted(true);
            return ResponseEntity.ok(new Message("삭제되었습니다"));
        }
    }



    /////////////////////private method///////////////////////

    //클럽id Null 체크

    private Club loadClubByClubId(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) {
            log.info("failed to find Club with id : " + clubId);
            throw new NullPointerException("해당 클럽을 찾을 수 없습니다");
        } else if (club.getIsDeleted().equals(true)) {
            throw new NullPointerException("삭제된 클럽입니다");
        }
        return club;
    }

    private ClubJoinEntry loadClubJoinEntry(Long userId, Long clubId) {
        return clubJoinEntryRepository.findByUserIdAndClubId(userId, clubId).orElseThrow(
                () -> new NullPointerException("유저 클럽 참여정보를 찾을 수 없습니다")
        );
    }

    public Integer userOwnedClubCount(Long userId) {
        return clubRepository.countByOwnerIdAndIsDeletedFalse(userId);
    }
}
