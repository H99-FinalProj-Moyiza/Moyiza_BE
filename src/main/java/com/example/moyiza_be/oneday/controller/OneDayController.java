package com.example.moyiza_be.oneday.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.oneday.dto.OneDayRequestDto;
import com.example.moyiza_be.oneday.dto.OneDayUpdateRequestDto;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.service.OneDayService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> createOneDay(@RequestPart(value = "data") OneDayRequestDto requestDto,
                                          @RequestPart(value = "imageFile") MultipartFile storedFileUrl,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return oneDayService.createOneDay(requestDto, userDetails.getUser(), storedFileUrl);
    }
    // ReadAll
    @GetMapping
    public ResponseEntity<?> getOneDayList() {
        return oneDayService.getOneDayList();
    }
    // ReadOne
    @GetMapping("/{oneDayId}")
    public ResponseEntity<?> getOneDay(@PathVariable Long oneDayId) {
        return oneDayService.getOneDay(oneDayId);
    }
    // Update
    @PutMapping("/{oneDayId}")
    public ResponseEntity<?> updateOneDay(@PathVariable Long oneDayId, @RequestBody OneDayUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return oneDayService.updateOneDay(oneDayId, requestDto ,userDetails.getUser());
    }
    // Delete
    @DeleteMapping("/{oneDayId}")
    public ResponseEntity<?> deleteOneDay(@PathVariable Long oneDayId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return oneDayService.deleteOneDay(oneDayId, userDetails.getUser());
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
    public ResponseEntity<List<OneDay>> recommendByDistance(@RequestParam("lat") double nowLatitude, @RequestParam("lon") double nowLongitude) {
        return oneDayService.recommendByDistance(nowLatitude, nowLongitude);
    }
//    //거리기반 원데이 추천 테스트
//    @GetMapping("/{lat}/{lon}")
//    public ResponseEntity<List<OneDay>> recommendByDistanceTest(@PathVariable(name = "lat") double nowLatitude, @PathVariable(name = "lon") double nowLongitude){
//        return oneDayService.recommendByDistanceTest(nowLatitude, nowLongitude);
//    }
}
