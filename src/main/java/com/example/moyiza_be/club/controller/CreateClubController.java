package com.example.moyiza_be.club.controller;


import com.example.moyiza_be.club.dto.ClubResponseDto;
import com.example.moyiza_be.club.dto.CreateClubIdResponse;
import com.example.moyiza_be.club.dto.ResumeCreationDto;
import com.example.moyiza_be.club.dto.createclub.*;
import com.example.moyiza_be.club.entity.CreateClub;
import com.example.moyiza_be.club.service.CreateClubService;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club/create")
public class CreateClubController {
    private final CreateClubService createClubService;

    @PostMapping
    public ResponseEntity<?> initCreateClubId(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        /// user.getUserId 검증로직 (anonymous일 경우 getId exception처리)
        return createClubService.initCreateClubId(user.getId());
    }


    @GetMapping("/{createclub_id}")
    public ResponseEntity<ResumeCreationDto> getPreviousCreateClub(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        /// user.getUserId 검증로직 (anonymous일 경우 getId exception처리)
        return createClubService.getPreviousCreateClub(user.getId(), createclub_id);
    }

    @PutMapping("/{createclub_id}/category")
    public ResponseEntity<Message> setCategory(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestCategoryDto requestCategoryDto,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        return createClubService.setCategory(
                user, createclub_id, requestCategoryDto.getCategoryEnum()
        );
    }

    @PutMapping("/{createclub_id}/tag")
    public ResponseEntity<Message> setTag(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestTagDto requestTagDto,
            @PathVariable Long createclub_id
    ){

        User user = userDetails.getUser();
        return createClubService.setTag(
                user, createclub_id, requestTagDto.getTagEnumList()
        );
    }
    
    @PutMapping("/{createclub_id}/title")
    public ResponseEntity<Message> setTitle(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestTitleDto requestTitleDto,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        return createClubService.setTitle(
                user, createclub_id, requestTitleDto.getTitle()
        );
    }

    @PutMapping("/{createclub_id}/content")
    public ResponseEntity<Message> setContent(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestContentDto requestContentDto,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        return createClubService.setContent(
                user, createclub_id, requestContentDto.getContent()
        );
    }

    @PutMapping("/{createclub_id}/maxgroupsize")
    public ResponseEntity<Message> setMaxGroupSize(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long createclub_id,
            @RequestBody CreateRequestMaxSizeDto requestMaxSize
    ){
        User user = userDetails.getUser();
        return createClubService.setMaxGroupSize(user, createclub_id, requestMaxSize.getMaxGroupSize());
    }

    @PutMapping("/{createclub_id}/policy")
    public ResponseEntity<Message> setPolicy(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CreateRequestPolicyDto requestPolicy,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        return createClubService.setPolicy(
                user, createclub_id, requestPolicy.getAgePolicy(),requestPolicy.getGenderPolicy());
    }

    @PutMapping("/{createclub_id}/images")
    public ResponseEntity<Message> setImageList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart @Nullable List<MultipartFile> image,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        return createClubService.setImageList( user, createclub_id, image );
    }

    @PostMapping("/{createclub_id}/confirm")
    public ResponseEntity<ClubResponseDto> confirmCreation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long createclub_id
    ){
        User user = userDetails.getUser();
        return createClubService.confirmCreation(user, createclub_id);
    }



}
