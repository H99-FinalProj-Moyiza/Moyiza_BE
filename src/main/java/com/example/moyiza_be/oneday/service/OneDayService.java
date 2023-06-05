package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.OneDayDetailResponseDto;
import com.example.moyiza_be.oneday.dto.OneDayRequestDto;
import com.example.moyiza_be.oneday.dto.OneDayUpdateRequestDto;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import com.example.moyiza_be.oneday.repository.OneDayAttendantRepository;
import com.example.moyiza_be.oneday.repository.OneDayRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneDayService {
    private final OneDayRepository oneDayRepository;
    private final OneDayAttendantRepository attendantRepository;
    private final AwsS3Uploader awsS3Uploader;

    // 원데이 생성
    @Transactional
    public ResponseEntity<?> createOneDay (OneDayRequestDto requestDto, User user, MultipartFile imageFile) {
        // image 확인
        String storedFileUrl = "";
        if(!imageFile.isEmpty()) {
            storedFileUrl = awsS3Uploader.uploadFile(imageFile);
        }
        // 생성
        OneDay oneDay = new OneDay(requestDto, user.getId(), storedFileUrl);
        oneDay.setDeleted(false);
        oneDay.setAttendantsNum(1);
        oneDayRepository.saveAndFlush(oneDay);
        // 방장 추가
        OneDayAttendant oneDayAttendant = new OneDayAttendant(user.getId(), oneDay.getId());
        attendantRepository.save(oneDayAttendant);
        return new ResponseEntity<>("생성성공", HttpStatus.OK);
    }
    // 원데이 수정
    @Transactional
    public ResponseEntity<?> updateOneDay(Long id, OneDayUpdateRequestDto requestDto, User user) throws IOException {
        // 원데이 가져오기
        OneDay oneDay = oneDayRepository.findById(id).orElseThrow(()->new IllegalArgumentException("404 OneDay NOT FOUND"));
        // 존재하는 글인가
        if (oneDay.isDeleted()) {
            throw new IllegalArgumentException("404 Already Deleted");
        }
        // 이미지 처리
        String storedFileUrl = "";
        if (!Objects.isNull(requestDto.getImage()) && !requestDto.getImage().isEmpty()) {
            storedFileUrl = awsS3Uploader.uploadFile(requestDto.getImage());
        }
        // 작성자는 맞는가
        if (Objects.equals(user.getId(),oneDay.getOwnerId())) {
            oneDay.updateOneDay(requestDto);
//            removeCache(oneDay);
        } else {
            throw new IllegalArgumentException("401 UnAuthorized");
        }
        return new ResponseEntity<>("수정성공", HttpStatus.OK);
    }
    // 원데이 삭제
    @Transactional
    public ResponseEntity<?> deleteOneDay(Long oneDayId, User user) {
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()-> new IllegalArgumentException("404 oneDay NOT FOUND"));
        if (oneDay.isDeleted()) {
            throw new IllegalArgumentException("404 Already Deleted");
        }
        // 만료처리 추가해야 하는데...
        if (user.getId().equals(oneDay.getOwnerId())) {
            oneDayRepository.deleteById(oneDayId);
            oneDay.setDeleted(true);
        }
        return new ResponseEntity<>("삭제성공", HttpStatus.OK);
    }

    // 원데이 목록 조회
    public ResponseEntity<List<OneDay>> getOneDayList() {
        List<OneDay> oneDayList = oneDayRepository.findAll();
        return new ResponseEntity<>(oneDayList, HttpStatus.OK);
    }
    // 원데이 상세 조회
    @Transactional
    public ResponseEntity<?> getOneDay(long oneDayId) {
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()->new IllegalArgumentException("404 OneDay NOT FOUND"));
        List<OneDayAttendant> attendantList = attendantRepository.findByOneDayId(oneDayId);
        OneDayDetailResponseDto detailResponseDto = new OneDayDetailResponseDto(oneDay, attendantList,attendantList.size());
        return new ResponseEntity<>(detailResponseDto, HttpStatus.OK);
    }
    // 원데이 참석
    public ResponseEntity<?> joinOneDay(Long oneDayId, User user) {
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()-> new IllegalArgumentException("404 OneDay NOT FOUND"));
        if (attendantRepository.findByOneDayIdAndUserId(oneDayId, user.getId()) != null) {
            return new ResponseEntity<>(new Message("이미 참여된 원데이입니다."), HttpStatus.FORBIDDEN);
        }
        OneDayAttendant oneDayAttendant = new OneDayAttendant(oneDayId, user.getId());
        oneDay.oneDayAttend();
        attendantRepository.save(oneDayAttendant);
        return new ResponseEntity<>("참석되었습니다.", HttpStatus.OK);
    }
    // 원데이 참석 취소
    public ResponseEntity<?> cancelOneDay (Long oneDayId, User user) {
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()-> new IllegalArgumentException("404 OneDay NOT FOUND"));
        OneDayAttendant oneDayAttendant = attendantRepository.findByOneDayIdAndUserId(oneDayId, user.getId());
        if (oneDayAttendant != null) {
            oneDay.oneDayCancel();
            attendantRepository.delete(oneDayAttendant);
            return new ResponseEntity<>("취소되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("아직 참석하지 않았어요", HttpStatus.BAD_REQUEST);
        }
    }

    //거리기반 위치 추천
    public ResponseEntity<List<OneDay>> recommendByDistance(double nowLatitude, double nowLongitude) {
        String location = "POINT(" + nowLongitude + " " + nowLatitude + ")";
        List<OneDay> aroundOneDayList = oneDayRepository.findAroundOneDayList(location);
        return new ResponseEntity<>(aroundOneDayList, HttpStatus.OK);
    }

//    //거리기반 위치 추천 테스트
//    public ResponseEntity<List<OneDay>> recommendByDistanceTest(double nowLatitude, double nowLongitude) {
//        //m당 y 좌표 이동 값
//        double mForLatitude =(1 /(6371 * 1 * (Math.PI/180)))/1000;
//        //m당 x 좌표 이동 값
//        double mForLongitude =(1 /(6371 * 1 * (Math.PI/180)* Math.cos(Math.toRadians(nowLatitude))))/1000;
//
//        //현재 위치 기준 검색 거리 좌표
//        double maxY = nowLatitude + (1000 * mForLatitude);
//        double minY = nowLatitude - (1000 * mForLatitude);
//        double maxX = nowLongitude + (1000 * mForLongitude);
//        double minX = nowLongitude - (1000 * mForLongitude);
//
//        List<OneDay> aroundOnedayList = oneDayRepository.findAroundOneDayList(maxY, maxX, minY, minX);
//        return new ResponseEntity<>(aroundOnedayList, HttpStatus.OK);
//    }
}
