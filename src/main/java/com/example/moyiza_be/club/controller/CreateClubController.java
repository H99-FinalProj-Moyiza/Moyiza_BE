package com.example.moyiza_be.club.controller;


import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.club.dto.createclub.*;
import com.example.moyiza_be.club.service.CreateClubService;
import com.example.moyiza_be.common.security.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/club/create/{createclub_id}/category")
    public ResponseEntity<Message> setCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestCategoryDto requestCategoryDto
    ){
        User user = userDetails.getUser();
        return createClubService.setCategory(user, requestCategoryDto.getCategoryEnum());
    }

    @RequestMapping("/club/create/{createclub_id}/tag")
    public ResponseEntity<Message> setTag(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestTagDto requestTagDto
    ){
        User user = userDetails.getUser();
        return createClubService.setTag(user, requestTagDto.getTagEnumList());
    }
    
    @RequestMapping("/club/create/{creatclub_id}/title")
    public ResponseEntity<Message> setTitle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestTitleDto requestTitleDto
    ){
        User user = userDetails.getUser();
        return createClubService.setTitle(user, requestTitleDto.getTitle());
    }

    @RequestMapping("/club/create/{createclub_id}/content")
    public ResponseEntity<Message> setContent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestContentDto requestContentDto
    ){
        User user = userDetails.getUser();
        return createClubService.setContent(user, requestContentDto.getContent());
    }

    @RequestMapping("/club/create/{createclub_id}/restriction")
    public ResponseEntity<Message> setRestriction(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestPolicyDto requestPolicy
    ){
        User user = userDetails.getUser();
        return createClubService.setPolicy(
                user, requestPolicy.getAgePolicy(),requestPolicy.getGenderPolicy());
    }

    //완료 API부터 작성



}
