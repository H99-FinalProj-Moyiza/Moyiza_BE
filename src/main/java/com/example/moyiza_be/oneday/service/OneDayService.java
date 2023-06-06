package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.club.entity.ClubImageUrl;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.oneday.dto.onedaycreate.OneDayCreateConfirmDto;
import com.example.moyiza_be.oneday.dto.onedaycreate.OneDayCreatingResponseDto;
import com.example.moyiza_be.oneday.entity.BanOneDay;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.*;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import com.example.moyiza_be.oneday.repository.OneDayAttendantRepository;
import com.example.moyiza_be.oneday.repository.OneDayImageUrlRepository;
import com.example.moyiza_be.oneday.repository.OneDayRepository;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OneDayService {
    private final OneDayRepository oneDayRepository;
    private final OneDayAttendantRepository attendantRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final OneDayImageUrlRepository imageUrlRepository;

    // 원데이 생성
    public OneDayDetailResponse createOneDay(OneDayCreateConfirmDto confirmDto) {
        OneDay oneDay = new OneDay(confirmDto);
        oneDayRepository.saveAndFlush(oneDay);
        List<String> oneDayImageUrlList = imageUrlRepository.findAllById(Collections.singleton(confirmDto.getCreateOneDayId()))
                .stream()
                .peek(image->image.setOneDayId(oneDay.getId()))
                .map(OneDayImageUrl::getImageUrl)
                .toList();
        chatService.makeChat(oneDay.getId(), ChatTypeEnum.ONEDAY, oneDay.getOneDayTitle());
        oneDay.setDeleted(false);
        oneDay.setAttendantsNum(1);
        oneDayRepository.saveAndFlush(oneDay);
        // 방장 추가
        User owner = userRepository.findById(confirmDto.getOwnerId()).orElseThrow(()->new NullPointerException("Invalid User"));
        OneDayAttendant oneDayAttendant = new OneDayAttendant(oneDay, owner);
        attendantRepository.save(oneDayAttendant);
        return new OneDayDetailResponse(oneDay, oneDayImageUrlList);
    }
    // 원데이 상세 조회
    public ResponseEntity<OneDayDetailResponseDto> getOneDayDetail(Long oneDayId) {
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()-> new NullPointerException("404 OneDay NOT FOUND"));
        // 이미지 처리 어떻게 하지?
        List<String> oneDayImageUrlList= OneDayImageUrlRepository.findAllByOneDayId(oneDayId).stream().map(OneDayImageUrl::getImageUrl).toList();
        List<OneDayAttendant> attendantList = attendantRepository.findByOneDayId(oneDayId);
        OneDayDetailResponseDto responseDto = new OneDayDetailResponseDto(oneDay, oneDayImageUrlList, attendantList, attendantList.size());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    // 원데이 목록 조회
    public ResponseEntity<Page<OneDayListPageResponseDto>> getOneDayList(Pageable pageable, CategoryEnum category, String q) {
        Page<OneDayListPageResponseDto> responseList;
        if (category != null && q != null) {
            //카테고리와 검색어를 모두 입력한 경우
            responseList = oneDayRepository.findByCategoryAndDeletedFalseAndOneDayTitleContaining(pageable, category, q).map(OneDayListPageResponseDto::new);
        } else if (category != null) {
            //카테고리만 입력한 경우
            responseList = oneDayRepository.findByCategoryAndDeletedFalse(pageable, category).map(OneDayListPageResponseDto::new);
        } else if (q != null) {
            responseList = oneDayRepository.findByDeletedFalseAndOneDayTitleContaining(pageable, q).map(OneDayListPageResponseDto::new);
        } else {
            //카테고리와 검색어가 모두 입력되지 않은 경우 전체 클럽 조회
            responseList = oneDayRepository.findAllByDeletedFalse(pageable).map(OneDayListPageResponseDto::new);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    // 원데이 수정
    public ResponseEntity<?> updateOneDay(Long id, OneDayUpdateRequestDto requestDto, User user) throws IOException {
        // 원데이 가져오기
        OneDay oneDay = oneDayRepository.findById(id).orElseThrow(()->new IllegalArgumentException("404 OneDay NOT FOUND"));
        // 존재하는 글인가
        if (oneDay.getDeleted()) {
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
    public ResponseEntity<Message> deleteOneDay(User user, Long oneDayId) {
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()->new NullPointerException("존재하는 하루속이 아니에요"));
        if (oneDay.getDeleted()==true) {
            throw new NullPointerException("삭제된 하루속입니다.");
        }else{
            if(!oneDay.getOwnerId().equals(user.getId())){
                return new ResponseEntity<>(new Message("내 하루속이 아닙니다"), HttpStatus.UNAUTHORIZED);
            }
            else{
                oneDay.setDeleted(true);
                return ResponseEntity.ok(new Message("삭제되었습니다"));
            }
        }
    }
    // 원데이 참석
    public ResponseEntity<?> joinOneDay(Long oneDayId, User user) {
        if(attendantRepository.existsByOneDayIdAndUserId(oneDayId, user.getId())){
            return new ResponseEntity<>(new Message("중복으로 가입할 수 없습니다"), HttpStatus.BAD_REQUEST);
        }
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()-> new NullPointerException("404 하루속 NOT FOUND"));
        if (oneDay.getType()== OneDayTypeEnum.APPROVAL) {
            return new ResponseEntity<>("승인제 구현중",HttpStatus.FORBIDDEN);
        } else {
            // 선착순제 인 경우 인원이 다 찾는가?
            if(oneDay.getAttendantsNum() < oneDay.getOneDayGroupSize()) {
                OneDayAttendant attendant = new OneDayAttendant(oneDay, user);
                attendantRepository.save(attendant);
                chatService.joinChat(oneDayId, ChatTypeEnum.ONEDAY, user);
                return new ResponseEntity<>("참석되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("하루속 인원이 가득 찾어요", HttpStatus.FORBIDDEN);
            }
        }
    }
    // 원데이 참석 취소
    public ResponseEntity<?> cancelOneDay(Long oneDayId, User user) {
        OneDayAttendant oneDayAttendant = attendantRepository.findByOneDayIdAndUserId(oneDayId, user.getId());
        if (oneDayAttendant != null) {
            attendantRepository.delete(oneDayAttendant);
            chatService.leaveChat(oneDayId, ChatTypeEnum.ONEDAY, user);
            return new ResponseEntity<>("하루속에서 탈퇴되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("하루속이 존재하지 않거나, 참석 정보가 없습니다.", HttpStatus.OK);
        }
    }
    // 원데이 강퇴
    public ResponseEntity<?> banOneDay(Long oneDayId, Long userId, BanOneDay banRequest) {
        if(!oneDayRepository.existsByIdAndDeletedFalseAndOwnerIdEquals(oneDayId, userId)){
            return new ResponseEntity<>(new Message("권한이 없거나, 하루속이 없습니다"), HttpStatus.BAD_REQUEST);
        }
        OneDayAttendant attendant = attendantRepository.findByOneDayIdAndUserId(banRequest.getBanUserId(), oneDayId);
        if (attendant != null) {
            attendantRepository.delete(attendant);
            return new ResponseEntity<>(String.format("user %d 가 하루속에서 강퇴되었습니다",banRequest.getBanUserId()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("하루속가입 정보가 없습니다.", HttpStatus.NOT_FOUND);
        }
    }

    // 원데이 승인제
    private ResponseEntity<?> approvalOneDay (Long oneDayId, Long userId, Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(()->new NullPointerException("생성된 하루속이 없어요!"));
        User guest = userRepository.findById(userId).orElseThrow(()->new NullPointerException("참석을 신청해 주세요"));
        OneDay oneDay = oneDayRepository.findById(oneDayId).orElseThrow(()->new NullPointerException("No Such ONEDAY"));
        if (ownerId.equals(userId)) {
            return new ResponseEntity<>("Hey! You own this!",HttpStatus.UNAUTHORIZED);
        } else {
            // 승인 푸시 생성
            return new ResponseEntity<>("승인 요청되었습니다.",HttpStatus.OK);
        }
    }

    //거리기반 위치 추천
    public ResponseEntity<List<OneDay>> recommendByDistance(double nowLatitude, double nowLongitude) {
//        String location = "POINT(" + nowLongitude + " " + nowLatitude + ")";
        List<OneDay> aroundOneDayList = oneDayRepository.findAllByOneDayLatitudeAndOneDayLongitude(nowLatitude, nowLongitude);
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
