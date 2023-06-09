package com.example.moyiza_be.oneday.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.onedaycreate.*;
import com.example.moyiza_be.oneday.service.OneDayCreateService;
import com.example.moyiza_be.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oneday/create")
public class OneDayCreateController {
    private final OneDayCreateService oneDayCreateService;
    // initOneDay
    @PostMapping
    public ResponseEntity<?> initCreateOneDay(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return oneDayCreateService.initCreateOneDay(user.getId());
    }
    // getTmp
    @GetMapping("/{oneDayTmpId}")
    public ResponseEntity<?> getExistCreatingOneDay(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.getExistCreatingOneDay(user.getId(), oneDayTmpId);
    }
    // title
    @PutMapping("/{oneDayTmpId}/title")
    public ResponseEntity<?> setTitle(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RequestTitleDto title, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.setTitle(user.getId(), oneDayTmpId, title);
    }
    // content
    @PutMapping("/{oneDayTmpId}/content")
    public ResponseEntity<?> setContent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RequestContentDto requestContentDto, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.setContent(user.getId(), oneDayTmpId, requestContentDto);
    }
    // category
    @PutMapping("/{oneDayTmpId}/category")
    public ResponseEntity<?> setCategory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RequestCategoryDto category, @PathVariable Long oneDayTmpId) {
        User user = userDetails.getUser();
        return oneDayCreateService.setCategory(user.getId(), oneDayTmpId, category);
    }
    // tagString
    @PutMapping("/{oneDayTmpId}/tag")
    public ResponseEntity<?> setTag(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RequestTagDto tag, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.setTag(user.getId(), oneDayTmpId, tag);
    }
    // policy
    @PutMapping("/{oneDayTmpId}/policy")
    public ResponseEntity<?> setPolicy(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody RequestPolicyDto requestPolicy, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.setPolicy(user.getId(), oneDayTmpId, requestPolicy);
    }
    // groupSize
    @PutMapping("/{oneDayTmpId}/maxgroupsize")
    public ResponseEntity<Message> setMaxGroupSize(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long oneDayTmpId,@RequestBody RequestSizeDto maxSize){
        User user = userDetails.getUser();
        return oneDayCreateService.setMaxGroupSize(user.getId(),oneDayTmpId,maxSize);
    }
    // location
    @PutMapping("/{oneDayTmpId}/location")
    public ResponseEntity<Message> setLocation(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long oneDayTmpId, @RequestBody RequestLocationDto locationDto){
        User user = userDetails.getUser();
        return oneDayCreateService.setLocation(user.getId(),oneDayTmpId,locationDto);
    }
    // startTime
    @PutMapping("/{oneDayTmpId}/time")
    public ResponseEntity<Message> setTime(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long oneDayTmpId, @RequestBody RequestDateDto time){
        User user = userDetails.getUser();
        if(time.getOneDayStartTime() == null){
            throw new IllegalArgumentException("oneday 시작시간이 없습니다");
        }
        return oneDayCreateService.setDate(user.getId(),oneDayTmpId,time);
    }
    // startTime
    @PutMapping("/{oneDayTmpId}/type")
    public ResponseEntity<Message> setType(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long oneDayTmpId, @RequestBody RequestTypeDto type){
        User user = userDetails.getUser();
        return oneDayCreateService.setType(user.getId(),oneDayTmpId,type);
    }
    // image
    @PutMapping("/{oneDayTmpId}/images")
    public ResponseEntity<Message> setImageList(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart @Nullable List<MultipartFile> image, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.setImageList( user.getId(), oneDayTmpId, image );
    }
    // confirm
    @PostMapping("/{oneDayTmpId}/confirm")
    public ResponseEntity<?> confirmCreation(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long oneDayTmpId){
        User user = userDetails.getUser();
        return oneDayCreateService.confirmCreation(user, oneDayTmpId);
    }
}
