package com.example.moyiza_be.club.service;

import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.club.dto.ClubMemberResponse;
import com.example.moyiza_be.club.dto.ConfirmClubCreationDto;
import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.entity.ClubImageUrl;
import com.example.moyiza_be.club.entity.ClubJoinEntry;
import com.example.moyiza_be.club.repository.ClubImageUrlRepository;
import com.example.moyiza_be.club.repository.ClubJoinEntryRepository;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubJoinEntryRepository clubJoinEntryRepository;
    private final UserService userService;
    private final ClubImageUrlRepository clubImageUrlRepository;


    //클럽 가입
    public ResponseEntity<Message> joinClub(Long clubId, User user) {
        if(clubJoinEntryRepository.existsByClubIdAndUserId(clubId, user.getId())){
            return new ResponseEntity<>(new Message("중복으로 가입할 수 없습니다"), HttpStatus.BAD_REQUEST);
        }
        ClubJoinEntry joinEntry = new ClubJoinEntry(user.getId(), clubId);
        clubJoinEntryRepository.save(joinEntry);
        //미래에 조건검증 추가
        Message message = new Message("가입이 승인되었습니다.");
        return ResponseEntity.ok(message);
    }

    //클럽 리스트 조회(전체조회, 검색조회 포함)
    public ResponseEntity<Page<ClubListResponse>> getClubList(Pageable pageable, CategoryEnum category, String q) {
        Page<ClubListResponse> responseList;
        if (category != null && q != null) {
            //카테고리와 검색어를 모두 입력한 경우
             responseList = clubRepository.findByCategoryAndTitleContaining(pageable, category, q).map(ClubListResponse::new);
        } else if (category != null) {
            //카테고리만 입력한 경우
            responseList = clubRepository.findByCategory(pageable, category).map(ClubListResponse::new);
        } else if (q != null) {
            responseList = clubRepository.findByTitleContaining(pageable, q).map(ClubListResponse::new);
        } else {
            //카테고리와 검색어가 모두 입력되지 않은 경우 전체 클럽 조회
            responseList = clubRepository.findAll(pageable).map(ClubListResponse::new);
        }
        return ResponseEntity.ok(responseList);
    }


    //클럽 상세 조회
    public ResponseEntity<ClubDetailResponse> getClubDetail(Long clubId) {
        Club club = loadClubByClubId(clubId);
        List<String> clubImageUrlList= clubImageUrlRepository.findAllByClubId(clubId).stream().map(ClubImageUrl::getImageUrl).toList();
        ClubDetailResponse responseDto = new ClubDetailResponse(club, clubImageUrlList);
        return ResponseEntity.ok(responseDto);
    }


    //클럽 멤버 조회
    //프로필사진, 닉네임, 클럽 가입 날짜
    public ResponseEntity<List<ClubMemberResponse>> getClubMember(Long clubId) {
        //queryDSL 적용 할 때 갈아엎어야함 (쿼리나가는거, 성능 계산해보고 결정)

        List<ClubJoinEntry> joinEntryList = clubJoinEntryRepository.findByClubId(clubId);
        Map<Long, LocalDateTime> joinEntryMap = new HashMap<>(); // inspection ??
        List<Long> userIdList = joinEntryList.stream()
                .peek(entry -> joinEntryMap.put(entry.getUserId(), entry.getCreatedAt()))
                .map(ClubJoinEntry::getUserId)
                .toList();

        List<User> memberList = userService.loadUserListByIdList(userIdList);

        List<ClubMemberResponse> clubMemberResponseList = memberList.stream()
                .map(member -> new ClubMemberResponse(member, joinEntryMap.get(member.getId())))
                .toList();

        return ResponseEntity.ok(clubMemberResponseList);
    }

    //클럽 탈퇴
    public ResponseEntity<Message> goodbyeClub(Long clubId, User user) {
        loadClubByClubId(clubId);

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
        loadClubByClubId(clubId);

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
    public ClubDetailResponse createClub(ConfirmClubCreationDto creationRequest){
        Club club = new Club(creationRequest);
        clubRepository.saveAndFlush(club);
        List<String> clubImageUrlList = clubImageUrlRepository.findAllByClubId(creationRequest.getCreateClubId())
                .stream()
                .peek(image->image.setClubId(club.getId()))
                .map(ClubImageUrl::getImageUrl)
                .toList();
        return new ClubDetailResponse(club, clubImageUrlList); // querydsl에서 List로 projection이 가능한가 확인해봐야함
    }

    ////////////////////private method///////////////////////

    //클럽id Null 체크
    private Club loadClubByClubId(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if(club == null){
            log.info("failed to find Club with id : " + clubId);
            throw new NullPointerException("해당 클럽을 찾을 수 없습니다");
        }
        else{return club;}
    }

    public Integer userOwnedClubCount(Long userId) {
        return clubRepository.countByOwnerIdAndIsDeletedFalse(userId);
    }
}
