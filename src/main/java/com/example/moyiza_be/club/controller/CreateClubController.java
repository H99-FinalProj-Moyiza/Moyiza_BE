package com.example.moyiza_be.club.controller;


import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.club.service.CreateClubService;
import com.example.moyiza_be.common.security.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club/create")
public class CreateClubController {
    private final CreateClubService createClubService;

    @RequestMapping("/club/create")
    public ResponseEntity<CreateClubIdResponse> initCreateClubId(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        return createClubService.initCreateClubId(user);
    }

    @RequestMapping("/club/create/{createclub-id}/category")
    public ResponseEntity<Message> setCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return null;
    }
}
