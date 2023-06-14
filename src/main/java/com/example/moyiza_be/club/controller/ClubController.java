package com.example.moyiza_be.club.controller;

import com.example.moyiza_be.club.dto.BanRequest;
import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.club.dto.ClubMemberResponse;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.event.dto.EventSimpleDetailDto;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {

    private final ClubService clubService;

    //Join Club
    @PostMapping("/{club_id}/join")
    public ResponseEntity<Message> joinClub(@PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return clubService.joinClub(club_id, userDetails.getUser());
    }

    //Get Club List
    //It's a controller that doesn't need to be there because it's calling a method like search, so there's logic to show it as default in the main.
    @GetMapping
    public ResponseEntity<Page<ClubListResponse>> getClubList(
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails == null ? null : userDetails.getUser();
        return clubService.getClubList(pageable, null, null, null, null, null, user);
    }

    //Search Club List
    @GetMapping("/search")
    public ResponseEntity<Page<ClubListResponse>> searchClubList(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String tag1,
            @RequestParam(required = false) String tag2,
            @RequestParam(required = false) String tag3,
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails == null ? null : userDetails.getUser();
        return clubService.getClubList(pageable, CategoryEnum.fromString(category), q, tag1, tag2, tag3, user);
    }


    //Get Club Details
    @GetMapping("/{club_id}")
    public ResponseEntity<ClubDetailResponse> getClub(
            @PathVariable Long club_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails == null ? null : userDetails.getUser();
        return clubService.getClubDetail(club_id, user);
    }


    //Get Club Member
    @GetMapping("/{club_id}/members")
    public ResponseEntity<List<ClubMemberResponse>> getClubMember(@PathVariable Long club_id) {
        return clubService.getClubMember(club_id);
    }

    //Leave Club
    @PostMapping("/{club_id}/goodbye")
    public ResponseEntity<Message> goodbyeClub(@PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return clubService.goodbyeClub(club_id, userDetails.getUser());
    }

    //Ban Club
    @PostMapping("/{club_id}/ban")
    public ResponseEntity<Message> banClub(
            @PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody BanRequest banRequest
    ) {
        User user = userDetails.getUser();
        return clubService.banClub(club_id, user, banRequest);
    }

    //Get Club Event List
    @GetMapping("/{club_id}/eventlist")
    public ResponseEntity<List<EventSimpleDetailDto>> getClubEventList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long club_id
    ) {
        User user = userDetails.getUser();
        return clubService.getClubEventList(user, club_id);
    }

    //Delete Club
    @DeleteMapping("/{club_id}/delete")
    public ResponseEntity<Message> deleteClub(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long club_id
    ) {
        User user = userDetails.getUser();
        return clubService.deleteClub(user, club_id);
    }

    @PostMapping("/{club_id}/like")
    public ResponseEntity<Message> likeClub(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long club_id
    ){
        User user = userDetails.getUser();
        return clubService.likeClub(user, club_id);
    }

    @DeleteMapping("/{club_id}/like")
    public ResponseEntity<Message> cancelLikeClub(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long club_id
    ){
        User user = userDetails.getUser();
        return clubService.cancelLikeClub(user, club_id);
    }
}
