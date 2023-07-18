package com.example.moyiza_be.domain.oneday.controller;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.domain.oneday.dto.OneDayImminentResponseDto;
import com.example.moyiza_be.domain.oneday.dto.OneDayUpdateRequestDto;
import com.example.moyiza_be.domain.oneday.dto.OneDayListResponseDto;
import com.example.moyiza_be.domain.oneday.dto.OneDayNearByResponseDto;
import com.example.moyiza_be.domain.oneday.service.OneDayService;
import com.example.moyiza_be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oneday")
public class OneDayController {
    private final OneDayService oneDayService;

//    @RequestMapping(value = "/", method = RequestMethod.POST,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
//    public OneDayDetailResponse createOneDay(@RequestPart(value = "data") OneDayCreateConfirmDto requestDto,
//                                             @RequestPart(value = "imageFile") MultipartFile storedFileUrl,
//                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
//        User user = userDetails.getUser();
//        return oneDayService.createOneDay(user, requestDto);
//    }
    // ReadAll
    @GetMapping
    public ResponseEntity<Page<OneDayListResponseDto>> getOneDayList(
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String tag1,
            @RequestParam(required = false) String tag2,
            @RequestParam(required = false) String tag3,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) LocalDateTime startafter,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails == null ? null : userDetails.getUser();
        return oneDayService.getFilteredOneDayList(
                user, pageable, null, null, null, null, null, null, null, null
        );
    }

    @GetMapping ("/search")
    public ResponseEntity<Page<OneDayListResponseDto>> searchOneDayList(
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String tag1,
            @RequestParam(required = false) String tag2,
            @RequestParam(required = false) String tag3,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double radius,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ){
        User user = userDetails == null ? null : userDetails.getUser();
        return oneDayService.getFilteredOneDayList(
                user, pageable, CategoryEnum.fromString(category), q, tag1, tag2, tag3, longitude, latitude, radius
        );
    }

    // ReadOne
    @GetMapping("/{oneDayId}")
    public ResponseEntity<?> getOneDay(
            @PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails == null ? null : userDetails.getUser();
        return oneDayService.getOneDayDetail(oneDayId, user);
    }
    // Update
    @PutMapping(value = "/{oneDayId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateOneDay(@RequestPart(value = "data") OneDayUpdateRequestDto requestDto,
                                          @RequestPart(value = "imageFile") MultipartFile storedFileUrl, @PathVariable(value = "oneDayId") Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return oneDayService.updateOneDay(oneDayId, requestDto ,userDetails.getUser(), storedFileUrl);
    }
    // Delete
    @DeleteMapping("/{oneDayId}")
    public ResponseEntity<?> deleteOneDay(@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return oneDayService.deleteOneDay(user.getId(), oneDayId);
    }
    // Attend
    @PostMapping("/{oneDayId}/join")
    public ResponseEntity<?> joinOneDay(@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return oneDayService.joinOneDay(oneDayId, userDetails.getUser());
    }
    // CancelAttend
    @DeleteMapping("/{oneDayId}/join")
    public ResponseEntity<?> cancelOneDay(@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return oneDayService.cancelOneDay(oneDayId, userDetails.getUser());
    }

    @PostMapping("/{onedayId}/like")
    public ResponseEntity<Message> likeOneday(
            @PathVariable Long onedayId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        User user = userDetails.getUser();
        return oneDayService.likeOneday(onedayId, user);
    }

    @DeleteMapping("/{onedayId}/like")
    public ResponseEntity<Message> cancelLikeOneday(
            @PathVariable Long onedayId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        User user = userDetails.getUser();
        return oneDayService.cancelLikeOneday(onedayId, user);
    }

    // Recommendation Based On Distance
    @GetMapping("/recommend")
    public ResponseEntity<List<OneDayNearByResponseDto>> recommendByDistance(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @RequestParam("lat") double nowLatitude, @RequestParam("lon") double nowLongitude) {
        User user = userDetails == null ? null : userDetails.getUser();
        return oneDayService.recommendByDistance(user, nowLatitude, nowLongitude);
    }

    // JoinWishList
    @GetMapping("/{oneDayId}/joinlist")
    public ResponseEntity<?> joinWishList(@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return oneDayService.joinWishList(oneDayId, userDetails.getUser());
    }
    // Join Approve
    @PostMapping("/{oneDayId}/joinlist/{userId}")
    public ResponseEntity<?> approveJoin(@PathVariable Long oneDayId, @PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails)  {
        return oneDayService.approveJoin(oneDayId, userId, userDetails.getUser());
    }
    // reject Approve
    @DeleteMapping("/{oneDayId}/joinlist/{userId}")
    public ResponseEntity<?> rejectJoin(@PathVariable Long oneDayId, @PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return oneDayService.rejectJoin(oneDayId, userId, userDetails.getUser());
    }

    @GetMapping("/imminent")
    public ResponseEntity<List<OneDayImminentResponseDto>> imminentOneDays(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails == null ? null : userDetails.getUser();
        return oneDayService.getImminentOneDays(user);
    }
    @GetMapping("/popular")
    public ResponseEntity<?> mostLikedOneDays(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails == null ? null : userDetails.getUser();
        return oneDayService.getMostLikedOneDays(user);
    }

}
