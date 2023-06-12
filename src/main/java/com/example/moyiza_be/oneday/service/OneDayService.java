package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.oneday.dto.*;
import com.example.moyiza_be.oneday.dto.onedaycreate.OneDayCreateConfirmDto;
import com.example.moyiza_be.oneday.entity.BanOneDay;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import com.example.moyiza_be.oneday.repository.OneDayAttendantRepository;
import com.example.moyiza_be.oneday.repository.OneDayImageUrlRepository;
import com.example.moyiza_be.oneday.repository.OneDayRepository;
import com.example.moyiza_be.oneday.repository.QueryDSL.OneDayRepositoryCustom;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OneDayService {
    private final OneDayRepository oneDayRepository;
    private final OneDayAttendantRepository attendantRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ChatService chatService;
    private final UserService userService;
    private final OneDayImageUrlRepository imageUrlRepository;
    private final OneDayRepositoryCustom oneDayRepositoryCustom;

    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";

    // 원데이 생성
    //revisit
    public OneDayDetailResponse createOneDay(User user, OneDayCreateConfirmDto confirmDto) {
        OneDay oneDay = new OneDay(confirmDto);
        List<String> oneDayImageUrlList = imageUrlRepository.findAllById(Collections.singleton(confirmDto.getCreateOneDayId()))
                .stream()
                .peek(image -> image.setOneDayId(oneDay.getId()))
                .map(OneDayImageUrl::getImageUrl)
                .toList();
        oneDay.setDeleted(false);
        oneDay.setAttendantsNum(1);
        oneDayRepository.saveAndFlush(oneDay);
        chatService.makeChat(oneDay.getId(), ChatTypeEnum.ONEDAY, oneDay.getOneDayTitle());
        // 방장 추가
        joinOneDay(oneDay.getId(), user);
        return new OneDayDetailResponse(oneDay, oneDayImageUrlList);
    }

    // 원데이 상세 조회
    public ResponseEntity<OneDayDetailResponseDto> getOneDayDetail(Long oneDayId) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        // 이미지 처리 어떻게 하지?
        List<String> oneDayImageUrlList = imageUrlRepository.findAllByOneDayId(oneDayId).stream().map(OneDayImageUrl::getImageUrl).toList();
        List<OneDayAttendant> attendantList = attendantRepository.findAllByOneDayId(oneDayId);
        OneDayDetailResponseDto responseDto = new OneDayDetailResponseDto(oneDay, oneDayImageUrlList, attendantList, attendantList.size());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 원데이 목록 조회
    public ResponseEntity<Page<OneDayListResponseDto>> getFilteredOneDayList(
            Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3,
            Double longitude, Double latitude, Double radius
    ) {
        Page<OneDayListResponseDto> filteredOnedayList = oneDayRepositoryCustom.getFilteredOnedayList(
                pageable, category, q, tag1, tag2, tag3, longitude, latitude, radius
        );

        return ResponseEntity.ok(filteredOnedayList);
    }

    // 원데이 수정
    public ResponseEntity<?> updateOneDay(Long id, OneDayUpdateRequestDto requestDto, User user, MultipartFile imageUrl) throws IOException {
        // 삭제안된 원데이 가져오기
        OneDay oneDay = loadExistingOnedayById(id);
        if (!checkOnedayOwnership(user.getId(), oneDay)) {
            throw new AccessDeniedException("권한이 없습니다");
        }
        // 이미지 처리
        // 이미지가 바뀌지 않아도 업로드 되는 이슈
        String storedFileUrl = oneDay.getOneDayImage();
        if (!Objects.isNull(imageUrl) && !imageUrl.isEmpty()) {
            storedFileUrl = awsS3Uploader.uploadFile(imageUrl);
        }
        // 작성자는 맞는가
        oneDay.updateAll(requestDto);
        oneDay.updateOneDayImage(storedFileUrl);
        return new ResponseEntity<>("수정성공", HttpStatus.OK);
    }

    // 원데이 삭제
    public ResponseEntity<Message> deleteOneDay(Long userId, Long oneDayId) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        checkOnedayOwnership(userId, oneDay);
        oneDay.setDeleted(true);
        return ResponseEntity.ok(new Message("삭제되었습니다"));

    }

    // 원데이 참석
    public ResponseEntity<?> joinOneDay(Long oneDayId, User user) {
        if (attendantRepository.existsByOneDayIdAndUserId(oneDayId, user.getId())) {
            return new ResponseEntity<>(new Message("중복으로 가입할 수 없습니다"), HttpStatus.BAD_REQUEST);
        }
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        if (oneDay.getType() == OneDayTypeEnum.APPROVAL) { // 방장일경우 Pass하는 로직 추가해야함
            return new ResponseEntity<>("승인제 구현중", HttpStatus.FORBIDDEN);
        } else {
            // 선착순제 인 경우 인원이 다 찼는가?
            if (oneDay.getAttendantsNum() < oneDay.getOneDayGroupSize()) {
                OneDayAttendant attendant = new OneDayAttendant(oneDay, user.getId());
                attendantRepository.save(attendant);
                chatService.joinChat(oneDayId, ChatTypeEnum.ONEDAY, user);
                oneDay.addAttendantNum();
                return new ResponseEntity<>("참석되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("하루속 인원이 가득 찼어요", HttpStatus.FORBIDDEN);
            }
        }
    }

    // 원데이 참석 취소
    public ResponseEntity<?> cancelOneDay(Long oneDayId, User user) {
        OneDay oneday = loadExistingOnedayById(oneDayId);
        OneDayAttendant oneDayAttendant = findAndLoadOnedayAttendant(oneDayId, user.getId());
        attendantRepository.delete(oneDayAttendant);
        chatService.leaveChat(oneDayId, ChatTypeEnum.ONEDAY, user);
        oneday.minusAttendantNum();
        return new ResponseEntity<>("하루속에서 탈퇴되었습니다.", HttpStatus.OK);
    }

    // 원데이 강퇴
    public ResponseEntity<?> banOneDay(Long oneDayId, Long userId, BanOneDay banRequest) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        if (!oneDayRepository.existsByIdAndDeletedFalseAndOwnerIdEquals(oneDayId, userId)) {
            return new ResponseEntity<>(new Message("권한이 없습니다"), HttpStatus.BAD_REQUEST);
        }
        OneDayAttendant attendant = findAndLoadOnedayAttendant(oneDayId, banRequest.getBanUserId());
        attendantRepository.delete(attendant);
        oneDay.minusAttendantNum();
        User banUser = userService.loadUserById(banRequest.getBanUserId());
        chatService.leaveChat(oneDayId, ChatTypeEnum.ONEDAY, banUser);
        return new ResponseEntity<>(String.format("user %d 가 하루속에서 강퇴되었습니다", banRequest.getBanUserId()), HttpStatus.BAD_REQUEST);
    }

    // 원데이 승인제
    //거리기반 위치 추천

    public ResponseEntity<List<OneDayNearByResponseDto>> recommendByDistance(double nowLatitude, double nowLongitude) {
        List<Object[]> nearByOneDays = oneDayRepository.findNearByOneDays(nowLatitude, nowLongitude);

        List<OneDayNearByResponseDto> oneDays = new ArrayList<>();
        for (Object[] row : nearByOneDays) {
            OneDay oneDay = (OneDay) row[0];
            double distance = (double) row[1];
            OneDayNearByResponseDto dto = new OneDayNearByResponseDto(oneDay, distance);
            oneDays.add(dto);
        }
        return new ResponseEntity<>(oneDays, HttpStatus.OK);
    }

    ///////////////////////////////////////////

    private OneDay loadExistingOnedayById(Long onedayId) {
        return oneDayRepository.findByIdAndDeletedFalse(onedayId).orElseThrow(
                () -> new NullPointerException("oneDay를 찾을 수 없습니다")
        );
    }

    private Boolean checkOnedayOwnership(Long userId, OneDay oneday) {
        return oneday.getOwnerId().equals(userId);
    }

    private OneDayAttendant findAndLoadOnedayAttendant(Long onedayId, Long userId) {
        return attendantRepository.findByOneDayIdAndUserId(onedayId, userId)
                .orElseThrow(() -> new NullPointerException("참여정보가 없습니다"));
    }

//    private ResponseEntity<?> approvalOneDay(Long oneDayId, Long userId, Long ownerId) {
//        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NullPointerException("생성된 하루속이 없어요!"));
//        User guest = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("참석을 신청해 주세요"));
//        OneDay oneDay = loadExistingOnedayById(oneDayId);
//        if (ownerId.equals(userId)) {
//            return new ResponseEntity<>("Hey! You own this!", HttpStatus.UNAUTHORIZED);
//        } else {
//            // 승인 푸시 생성
//            return new ResponseEntity<>("승인 요청되었습니다.", HttpStatus.OK);
//        }
//    }
}
