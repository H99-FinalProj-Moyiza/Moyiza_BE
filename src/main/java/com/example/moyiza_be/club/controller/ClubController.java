package com.example.moyiza_be.club.controller;

import com.example.moyiza_be.club.dto.BanRequest;
import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.club.dto.ClubMemberResponse;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.event.entity.Event;
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

    //클럽 가입
    @PostMapping("/{club_id}/join")
    public ResponseEntity<Message> joinClub(@PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return clubService.joinClub(club_id, userDetails.getUser());
    }

    //클럽 전체 조회
    //검색조회와 같은 메서드를 호출하고 있어서 없어도 되는 controller임// 메인에 default로 보여줄 때의 로직이 생길경우 사용하려고 놔둠
    @GetMapping
    public ResponseEntity<Page<ClubListResponse>> getClubList(@PageableDefault(page = 0, size = 8,
            sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return clubService.getClubList(pageable, null, null);
    }

    //클럽 검색 조회
    @GetMapping("/search")
    public ResponseEntity<Page<ClubListResponse>> searchClubList(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
                                                                ) {
        return clubService.getClubList(pageable, category == null? null : CategoryEnum.fromString(category), q);
    }


    //클럽 상세 조회
    @GetMapping("/{club_id}")
    public ResponseEntity<ClubDetailResponse> getClub(@PathVariable Long club_id) {
        return clubService.getClubDetail(club_id);
    }



    //클럽 멤버 조회
    @GetMapping("/{club_id}/members")
    public ResponseEntity<List<ClubMemberResponse>> getClubMember(@PathVariable Long club_id) {
        return clubService.getClubMember(club_id);
    }

    //클럽 이벤트 리스트 : 동현님이 만드심


    //클럽 탈퇴
    @PostMapping("/{club_id}/goodbye")
    public ResponseEntity<Message> goodbyeClub(@PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return clubService.goodbyeClub(club_id, userDetails.getUser());
    }

    //클럽 강퇴
    @PostMapping("/{club_id}/ban")
    public ResponseEntity<Message> banClub(
            @PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody BanRequest banRequest
    ) {
        User user = userDetails.getUser();
        return clubService.banClub(club_id, user.getId(), banRequest);
    }

    @GetMapping("/{club_id}/eventlist")
    public ResponseEntity<List<Event>> getClubEventList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long club_id
    ) {
        User user = userDetails.getUser();
        return clubService.getClubEventList(user, club_id);
    }

}
