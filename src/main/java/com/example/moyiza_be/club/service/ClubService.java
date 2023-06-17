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
import com.example.moyiza_be.event.service.EventService;
import com.example.moyiza_be.like.service.LikeService;
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
    private final LikeService likeService;


    //Join Club
    public ResponseEntity<Message> joinClub(Long clubId, User user) {
        Club club = loadClubByClubId(clubId);

        if (clubJoinEntryRepository.existsByClubIdAndUserId(clubId, user.getId())) {
            return new ResponseEntity<>(new Message("You can't sign up for duplicates."), HttpStatus.BAD_REQUEST);
        }

        club.addAttend();
        clubRepository.save(club);

        ClubJoinEntry joinEntry = new ClubJoinEntry(user.getId(), clubId);
        clubJoinEntryRepository.save(joinEntry);
        chatService.joinChat(clubId, ChatTypeEnum.CLUB, user);

        //Need to add conditional validation in the future
        Message message = new Message("Your signup has been approved.");
        return ResponseEntity.ok(message);
    }

    //Club List Search (including full and search)
    public ResponseEntity<Page<ClubListResponse>> getClubList(
            Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3, User user
    ) {
        Page<ClubListResponse> responseList = clubRepositoryCustom.filteredClubResponseList(
                pageable, category, q, tag1, tag2, tag3, user);
        return ResponseEntity.ok(responseList);
    }
  
 
    //Get Club Detail
    public ResponseEntity<ClubDetailResponse> getClubDetail(Long clubId, User user) {
        ClubDetailResponse clubDetailResponse = clubRepositoryCustom.getClubDetail(clubId, user);
        if(clubDetailResponse == null){
            throw new NullPointerException("Clubs not found.");
        }
        List<String> clubImageUrlList = clubImageUrlRepositoryCustom.getAllImageUrlByClubId(clubId);
        clubDetailResponse.setClubImageUrlList(clubImageUrlList);
        List<ClubMemberResponse> memberList = clubJoinEntryRepositoryCustom.getClubMemberList(clubId);
        clubDetailResponse.setMemberList(memberList);

        return ResponseEntity.ok(clubDetailResponse);
    }

    //Get Club List on Mypage
    public ClubListOnMyPage getClubListOnMyPage(Long userId, Long profileId) {
        List<ClubDetailResponse> clubsInOperationInfo = clubRepositoryCustom.getManagedClubDetail(userId, profileId);
        List<ClubDetailResponse> clubsInParticipatingInfo = clubRepositoryCustom.getJoinedClubDetail(userId, profileId);
        return new ClubListOnMyPage(clubsInOperationInfo, clubsInParticipatingInfo);
    }

    //Get Club Member
    public ResponseEntity<List<ClubMemberResponse>> getClubMember(Long clubId) {

        List<ClubMemberResponse> clubMemberResponseList = clubJoinEntryRepositoryCustom.getClubMemberList(clubId);

        return ResponseEntity.ok(clubMemberResponseList);
    }

    //Leave Club
    public ResponseEntity<Message> goodbyeClub(Long clubId, User user) {
        //Can't I use the query to get the number of members ? entity has no attend number.
        Club club = loadClubByClubId(clubId);
        ClubJoinEntry clubJoinEntry = loadClubJoinEntry(user.getId(), clubId);

        clubJoinEntryRepository.delete(clubJoinEntry);
        club.cancelAttend();
        clubRepository.saveAndFlush(club);
        chatService.leaveChat(clubId, ChatTypeEnum.CLUB, user);
        Message message = new Message("You've been removed from the club.");
        return ResponseEntity.ok(message);
    }

    //Ban Club
    public ResponseEntity<Message> banClub(Long clubId, User user, BanRequest banRequest) {
        if (!clubRepository.existsByIdAndIsDeletedFalseAndOwnerIdEquals(clubId, user.getId())) {
            throw new IllegalAccessError("You are not authorized.");
        }
        Club club = loadClubByClubId(clubId);
        ClubJoinEntry joinEntry = loadClubJoinEntry(banRequest.getBanUserId(), clubId);

        clubJoinEntryRepository.delete(joinEntry);
        club.cancelAttend();
        clubRepository.saveAndFlush(club);
        chatService.leaveChat(clubId, ChatTypeEnum.CLUB, user);

        log.info("user " + user.getId() + " banned user " + banRequest.getBanUserId() + " from club " + clubId);

        //Add logic here when adding post-deportation signup restrictions.
        Message message = new Message(String.format("User %d has been dropped from the club.", banRequest.getBanUserId()));
        return ResponseEntity.ok(message);
    }

    //Create Club
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
        return new ClubDetailResponse(club, clubImageUrlList, user); // Need to check if projection from querydsl to List is possible
    }

    public ResponseEntity<List<EventSimpleDetailDto>> getClubEventList(User user, Long clubId) {
        List<EventSimpleDetailDto> eventList = eventService.getEventList(clubId, user);
        return ResponseEntity.ok(eventList);
    }

    public ResponseEntity<Message> deleteClub(User user, Long clubId) {
        //Temporary implementation, logic changes may be required (softdelete ? orphanremoval ?)
        Club club = loadClubByClubId(clubId);
        if (!club.getOwnerId().equals(user.getId())) {
            return new ResponseEntity<>(new Message("Not my club."), HttpStatus.UNAUTHORIZED);
        } else {
            club.flagDeleted(true);
            return ResponseEntity.ok(new Message("Deleted."));
        }
    }

    public ResponseEntity<Message> likeClub(User user, Long clubId) {
        Club club = loadClubByClubId(clubId);
        ResponseEntity<Message> likeServiceResponse = likeService.clubLike(user.getId(), clubId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        club.addLike();
        return likeServiceResponse;
    }

    public ResponseEntity<Message> cancelLikeClub(User user, Long clubId){
        Club club = loadClubByClubId(clubId);
        ResponseEntity<Message> likeServiceResponse = likeService.cancelClubLike(user.getId(), clubId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        club.minusLike();
        return likeServiceResponse;
    }



    /////////////////////private method///////////////////////

    //Clubid Null Check

    private Club loadClubByClubId(Long clubId) {
        Club club = clubRepository.findById(clubId).orElse(null);
        if (club == null) {
            log.info("failed to find Club with id : " + clubId);
            throw new NullPointerException("The club was not found.");
        } else if (club.getIsDeleted().equals(true)) {
            throw new NullPointerException("Deleted club.");
        }
        return club;
    }

    private ClubJoinEntry loadClubJoinEntry(Long userId, Long clubId) {
        return clubJoinEntryRepository.findByUserIdAndClubId(userId, clubId).orElseThrow(
                () -> new NullPointerException("User's club participation information not found.")
        );
    }

    public Integer userOwnedClubCount(Long userId) {
        return clubRepository.countByOwnerIdAndIsDeletedFalse(userId);
    }

    public ResponseEntity<?> getMostLikedClub() {
        List<Club> clubList = clubRepository.findAllByIsDeletedFalseOrderByNumLikesDesc();
        List<ClubSimpleResponseDto> clubs = new ArrayList<>();
        for (Club club : clubList) {
            List<String> clubImageUrlList = clubImageUrlRepositoryCustom.getAllImageUrlByClubId(club.getId());
            ClubSimpleResponseDto clubDto = new ClubSimpleResponseDto(club, clubImageUrlList);
            clubs.add(clubDto);
        }
        return new ResponseEntity<>(clubs, HttpStatus.OK);
    }


}
