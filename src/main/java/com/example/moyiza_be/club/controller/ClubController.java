package com.example.moyiza_be.club.controller;

import com.example.moyiza_be.club.dto.ClubMemberDto;
import com.example.moyiza_be.club.dto.ClubResponseDto;
import com.example.moyiza_be.club.service.ClubService;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import lombok.RequiredArgsConstructor;
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
    @GetMapping
    public ResponseEntity<List<ClubResponseDto>> getClubList() {
        return clubService.getClubList();
    }

    //클럽 검색 조회
    public ResponseEntity<List<ClubResponseDto>> searchClubList(@RequestParam(required = false) String category,
                                                                @RequestParam(required = false) String q) {
        return clubService.searchClubList(category, q);
    }


    //클럽 상세 조회
    @GetMapping("/{club_id}")
    public ResponseEntity<ClubResponseDto> getClub(@PathVariable Long club_id) {
        return clubService.getClub(club_id);
    }



    //클럽 멤버 조회
    @GetMapping("/{club_id}/members")
    public ResponseEntity<List<ClubMemberDto>> getClubMember(@PathVariable Long club_id) {
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
    public ResponseEntity<Message> banClub(@PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return clubService.banclub(club_id, userDetails.getUser());
    }

}
