package com.example.moyiza_be.common.common_features.blackList.controller;


import com.example.moyiza_be.common.common_features.blackList.dto.BlackListMemberResponse;
import com.example.moyiza_be.common.common_features.blackList.service.BlackListService;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blackList")
public class BlackListController {

    private final BlackListService blackListService;

    @PostMapping("/{profileId}")
    public ResponseEntity<?> blackListing(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long profileId) {
        return blackListService.blackListing(userDetails.getUser(), profileId);
    }

    @GetMapping
    public ResponseEntity<List<BlackListMemberResponse>> getBlackList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blackListService.getBlackList(userDetails.getUser());
    }

}
