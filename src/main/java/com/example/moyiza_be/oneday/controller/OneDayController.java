package com.example.moyiza_be.oneday.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.oneday.dto.OneDayDetailResponse;
import com.example.moyiza_be.oneday.dto.OneDayNearByResponseDto;
import com.example.moyiza_be.oneday.dto.onedaycreate.OneDayCreateConfirmDto;
import com.example.moyiza_be.oneday.dto.OneDayUpdateRequestDto;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.service.OneDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oneday")
public class OneDayController {
    private final OneDayService oneDayService;

    // Create
    @RequestMapping(value = "/", method = RequestMethod.POST,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
//    @PostMapping
    public OneDayDetailResponse createOneDay(@RequestPart(value = "data") OneDayCreateConfirmDto requestDto,
                                             @RequestPart(value = "imageFile") MultipartFile storedFileUrl,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return oneDayService.createOneDay(requestDto);
    }
    // ReadAll
    @GetMapping
    public ResponseEntity<?> getOneDayList() {
        return oneDayService.getOneDayList(Pageable.unpaged(), null, null);
    }
    // ReadOne
    @GetMapping("/{oneDayId}")
    public ResponseEntity<?> getOneDay(@PathVariable Long oneDayId) {
        return oneDayService.getOneDayDetail(oneDayId);
    }
    // Update
    @RequestMapping(value = "/{oneDayId}", method = RequestMethod.PUT,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateOneDay(@RequestPart(value = "data")  OneDayUpdateRequestDto requestDto,
                                          @RequestPart(value = "imageFile") MultipartFile storedFileUrl,@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return oneDayService.updateOneDay(oneDayId, requestDto ,userDetails.getUser(), storedFileUrl);
    }
    // Delete
    @DeleteMapping("/{oneDayId}")
    public ResponseEntity<?> deleteOneDay(@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return oneDayService.deleteOneDay(userDetails.getUser(), oneDayId);
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

    //거리기반 원데이 추천
    @GetMapping("/recommend")
    public ResponseEntity<List<OneDayNearByResponseDto>> recommendByDistance(@RequestParam("lat") double nowLatitude, @RequestParam("lon") double nowLongitude) {
        return oneDayService.recommendByDistance(nowLatitude, nowLongitude);
    }

}
