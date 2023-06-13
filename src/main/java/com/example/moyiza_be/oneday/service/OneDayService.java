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
import com.example.moyiza_be.oneday.repository.QueryDSL.OneDayAttendantRepositoryCustom;
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
    private final OneDayAttendantRepositoryCustom oneDayAttendantRepositoryCustom;

    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";

    // Create OneDay
    // revisit

    @Transactional
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
        // Add Owner
        joinOneDay(oneDay.getId(), user);
        return new OneDayDetailResponse(oneDay, oneDayImageUrlList);
    }

    // Read Detail OneDay
    public ResponseEntity<OneDayDetailResponseDto> getOneDayDetail(Long oneDayId) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        // Image Process Issue at here...
        List<String> oneDayImageUrlList = imageUrlRepository.findAllByOneDayId(oneDayId).stream().map(OneDayImageUrl::getImageUrl).toList();
        List<OneDayMemberResponse> oneDayMemberResponseList = oneDayAttendantRepositoryCustom.getOneDayMemberList(oneDayId);
        OneDayDetailResponseDto responseDto = new OneDayDetailResponseDto(oneDay, oneDayImageUrlList, oneDayMemberResponseList);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Read OneDay List
    public ResponseEntity<Page<OneDayListResponseDto>> getFilteredOneDayList(
            Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3,
            Double longitude, Double latitude, Double radius, LocalDateTime startafter
    ) {
        Page<OneDayListResponseDto> filteredOnedayList = oneDayRepositoryCustom.getFilteredOnedayList(
                pageable, category, q, tag1, tag2, tag3, longitude, latitude, radius, startafter
        );
        return ResponseEntity.ok(filteredOnedayList);
    }

    // List For MyPage OneDay
    public OneDayListOnMyPage getOneDayListOnMyPage(Long userId) {
        // List For Operating OneDay
        List<OneDay> oneDaysInOperation = oneDayRepository.findAllByOwnerId(userId);
        List<OneDayDetailOnMyPage> oneDaysInOperationInfo = oneDaysInOperation.stream()
                .map(oneDay -> new OneDayDetailOnMyPage(oneDay).builder()
                        .oneDayId(oneDay.getId())
                        .oneDayTitle(oneDay.getOneDayTitle())
                        .oneDayContent(oneDay.getOneDayContent())
                        .oneDayLocation(oneDay.getOneDayLocation())
                        .category(oneDay.getCategory().getCategory())
                        .tagString(TagEnum.parseTag(oneDay.getTagString()))
                        .oneDayGroupSize(oneDay.getOneDayGroupSize())
                        .oneDayImage(oneDay.getOneDayImage())
                        .oneDayAttendantListSize(oneDaysInOperation.size())
                        .build())
                .collect(Collectors.toList());

        // List For Attending OneDay
        List<OneDayAttendant> oneDaysInParticipatingEntry = attendantRepository.findByUserId(userId);
        List<Long> oneDaysInParticipatingIds = oneDaysInParticipatingEntry.stream()
                .map(OneDayAttendant::getOneDayId)
                .collect(Collectors.toList());
        List<OneDay> oneDaysInParticipating = oneDayRepository.findAllByIdIn(oneDaysInParticipatingIds);
        List<OneDayDetailOnMyPage> oneDaysInParticipatingInfo = oneDaysInParticipating.stream()
                .map(oneDay -> new OneDayDetailOnMyPage(oneDay).builder()
                        .oneDayId(oneDay.getId())
                        .oneDayTitle(oneDay.getOneDayTitle())
                        .oneDayContent(oneDay.getOneDayContent())
                        .oneDayLocation(oneDay.getOneDayLocation())
                        .category(oneDay.getCategory().getCategory())
                        .tagString(TagEnum.parseTag(oneDay.getTagString()))
                        .oneDayGroupSize(oneDay.getOneDayGroupSize())
                        .oneDayImage(oneDay.getOneDayImage())
                        .oneDayAttendantListSize(oneDaysInParticipating.size())
                        .build())
                .collect(Collectors.toList());

        return new OneDayListOnMyPage(oneDaysInOperationInfo, oneDaysInParticipatingInfo);
    }

    // Update OneDay
    @Transactional
    public ResponseEntity<?> updateOneDay(Long id, OneDayUpdateRequestDto requestDto, User user, MultipartFile imageUrl) throws IOException {
        // Load Undeleted OneDay
        OneDay oneDay = loadExistingOnedayById(id);
        if (!checkOnedayOwnership(user.getId(), oneDay)) {
            throw new AccessDeniedException("401 Unauthorized");
        }
        // Imaging Process
        // Issue : Even Image not changed, Image Upload one more time
        String storedFileUrl = oneDay.getOneDayImage();
        if (!Objects.isNull(imageUrl) && !imageUrl.isEmpty()) {
            storedFileUrl = awsS3Uploader.uploadFile(imageUrl);
        }
        // Is User Author?
        oneDay.updateAll(requestDto);
        oneDay.updateOneDayImage(storedFileUrl);
        return new ResponseEntity<>("Update Complete", HttpStatus.OK);
    }

    // Deleting OneDay
    @Transactional
    public ResponseEntity<Message> deleteOneDay(Long userId, Long oneDayId) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        checkOnedayOwnership(userId, oneDay);
        oneDay.setDeleted(true);
        return ResponseEntity.ok(new Message("Deleting Complete"));

    }

    // Attending OneDay
    @Transactional
    public ResponseEntity<?> joinOneDay(Long oneDayId, User user) {
        if (attendantRepository.existsByOneDayIdAndUserId(oneDayId, user.getId())) {
            return new ResponseEntity<>(new Message("Cannot Attend Twice"), HttpStatus.BAD_REQUEST);
        }
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        if (oneDay.getType() == OneDayTypeEnum.APPROVAL) { // Logic should be added if user is owner
            return new ResponseEntity<>("Approval System Under Constructing", HttpStatus.FORBIDDEN);
        } else {
            // FCGSB -> Is it Fully occupied?
            if (oneDay.getAttendantsNum() < oneDay.getOneDayGroupSize()) {
                OneDayAttendant attendant = new OneDayAttendant(oneDay, user.getId());
                attendantRepository.save(attendant);
                chatService.joinChat(oneDayId, ChatTypeEnum.ONEDAY, user);
                oneDay.addAttendantNum();
                return new ResponseEntity<>("Attending Success", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Fully Occupied", HttpStatus.FORBIDDEN);
            }
        }
    }

    // Cancel OneDay Attend
    @Transactional
    public ResponseEntity<?> cancelOneDay(Long oneDayId, User user) {
        OneDay oneday = loadExistingOnedayById(oneDayId);
        OneDayAttendant oneDayAttendant = findAndLoadOnedayAttendant(oneDayId, user.getId());
        attendantRepository.delete(oneDayAttendant);
        chatService.leaveChat(oneDayId, ChatTypeEnum.ONEDAY, user);
        oneday.minusAttendantNum();
        return new ResponseEntity<>("cancel attending completed", HttpStatus.OK);
    }

    // Ban Person at OneDay
    @Transactional
    public ResponseEntity<?> banOneDay(Long oneDayId, Long userId, BanOneDay banRequest) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        if (!oneDayRepository.existsByIdAndDeletedFalseAndOwnerIdEquals(oneDayId, userId)) {
            return new ResponseEntity<>(new Message("401 Unauthorized"), HttpStatus.BAD_REQUEST);
        }
        OneDayAttendant attendant = findAndLoadOnedayAttendant(oneDayId, banRequest.getBanUserId());
        attendantRepository.delete(attendant);
        oneDay.minusAttendantNum();
        User banUser = userService.loadUserById(banRequest.getBanUserId());
        chatService.leaveChat(oneDayId, ChatTypeEnum.ONEDAY, banUser);
        return new ResponseEntity<>(String.format("user %d has been banned", banRequest.getBanUserId()), HttpStatus.BAD_REQUEST);
    }

    // Recommendation Based on Distance

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

    /////////////////////////////////////////

    private OneDay loadExistingOnedayById(Long onedayId) {
        return oneDayRepository.findByIdAndDeletedFalse(onedayId).orElseThrow(
                () -> new NullPointerException("404 OneDay Not Found")
        );
    }

    private Boolean checkOnedayOwnership(Long userId, OneDay oneday) {
        return oneday.getOwnerId().equals(userId);
    }

    private OneDayAttendant findAndLoadOnedayAttendant(Long onedayId, Long userId) {
        return attendantRepository.findByOneDayIdAndUserId(onedayId, userId)
                .orElseThrow(() -> new NullPointerException("No Such Attendant"));
    }

//      OneDay Approval System
//    private ResponseEntity<?> approvalOneDay(Long oneDayId, Long userId, Long ownerId) {
//        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NullPointerException("404 No Such Created OneDay!"));
//        User guest = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("Request Attending First"));
//        OneDay oneDay = loadExistingOneDayById(oneDayId);
//        if (ownerId.equals(userId)) {
//            return new ResponseEntity<>("Hey! You own this!", HttpStatus.UNAUTHORIZED);
//        } else {
//            // Approval Push Create
//            return new ResponseEntity<>("Approval Request Succeed", HttpStatus.OK);
//        }
//    }
}
