package com.example.moyiza_be.oneday.service;

import com.example.moyiza_be.alert.entity.Alert;
import com.example.moyiza_be.alert.repository.AlertRepository;
import com.example.moyiza_be.alert.service.AlertService;
import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.common.enums.*;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.like.service.LikeService;
import com.example.moyiza_be.oneday.dto.*;
import com.example.moyiza_be.oneday.dto.onedaycreate.OneDayCreateConfirmDto;
import com.example.moyiza_be.oneday.entity.*;
import com.example.moyiza_be.oneday.repository.OneDayApprovalRepository;
import com.example.moyiza_be.oneday.repository.OneDayAttendantRepository;
import com.example.moyiza_be.oneday.repository.OneDayImageUrlRepository;
import com.example.moyiza_be.oneday.repository.OneDayRepository;
import com.example.moyiza_be.oneday.repository.QueryDSL.OneDayAttendantRepositoryCustom;
import com.example.moyiza_be.oneday.repository.QueryDSL.OneDayRepositoryCustom;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OneDayService {
    private final OneDayRepository oneDayRepository;
    private final OneDayAttendantRepository attendantRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;
    private final AlertService alertService;
    private final AwsS3Uploader awsS3Uploader;
    private final ChatService chatService;
    private final UserService userService;
    private final OneDayImageUrlRepository imageUrlRepository;
    private final OneDayApprovalRepository approvalRepository;
    private final OneDayRepositoryCustom oneDayRepositoryCustom;
    private final OneDayAttendantRepositoryCustom oneDayAttendantRepositoryCustom;
    private final LikeService likeService;

    private final static String DEFAULT_IMAGE_URL = "https://res.cloudinary.com/dsav9fenu/image/upload/v1684890347/KakaoTalk_Photo_2023-05-24-10-04-52_ubgcug.png";
    private final LocalDateTime timeNow = LocalDateTime.now();

    // Create OneDay
    // revisit

    public OneDayDetailResponse createOneDay(User user, OneDayCreateConfirmDto confirmDto) {
        OneDay oneDay = new OneDay(confirmDto);
//        List<String> oneDayImageUrlList = imageUrlRepository.findAllById(Collections.singleton(confirmDto.getCreateOneDayId()))
//                .stream()
//                .peek(image -> image.setOneDayId(oneDay.getId()))
//                .map(OneDayImageUrl::getImageUrl)
//                .toList();
//        oneDayImageUrlList.forEach(image -> image.setOneDayId(oneDay.getId()));
//        imageUrlRepository.saveAll(oneDayImageUrlList);
        oneDay.setDeleted(false);
        oneDayRepository.saveAndFlush(oneDay);
        List<OneDayImageUrl> oneDayImageUrlList = imageUrlRepository.findAllByOneDayCreateId(confirmDto.getCreateOneDayId());
        for (OneDayImageUrl image : oneDayImageUrlList) {
            log.info("Image's OneDayID : " + image.getOneDayId() + ", oneDay.getId() : " + oneDay.getId());
            image.setOneDayId(oneDay.getId());
            imageUrlRepository.save(image);
        }
        chatService.makeChat(oneDay.getId(), ChatTypeEnum.ONEDAY, oneDay.getOneDayTitle());
        // Add Owner
        joinOneDay(oneDay.getId(), user);
        return new OneDayDetailResponse(oneDay, oneDayImageUrlList);
    }

    // Read Detail OneDay
    public ResponseEntity<OneDayDetailResponseDto> getOneDayDetail(Long oneDayId, User user) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        // Image Process Issue at here...
        List<String> oneDayImageUrlList = imageUrlRepository.findAllByOneDayId(oneDayId).stream().map(OneDayImageUrl::getImageUrl).toList();
        List<MemberResponse> memberResponseList = oneDayAttendantRepositoryCustom.getOneDayMemberList(oneDayId);
        Boolean isLikedByUser = likeService.checkLikeExists(user.getId(), oneDayId, LikeTypeEnum.ONEDAY);
        User owner = userService.loadUserById(oneDay.getOwnerId());
        OneDayDetailResponseDto responseDto = new OneDayDetailResponseDto(
                owner, oneDay, oneDayImageUrlList, memberResponseList, isLikedByUser);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Read OneDay List
    public ResponseEntity<Page<OneDayListResponseDto>> getFilteredOneDayList(
            User user, Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3,
            Double longitude, Double latitude, Double radius, LocalDateTime startafter
    ) {
        Page<OneDayListResponseDto> filteredOnedayList = oneDayRepositoryCustom.getFilteredOnedayList(
                user, pageable, category, q, tag1, tag2, tag3, longitude, latitude, radius, startafter
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
                .sorted(Comparator.comparing(OneDayDetailOnMyPage::getOneDayId).reversed())
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
                .sorted(Comparator.comparing(OneDayDetailOnMyPage::getOneDayId).reversed())
                .collect(Collectors.toList());

        return new OneDayListOnMyPage(oneDaysInOperationInfo, oneDaysInParticipatingInfo);
    }

    // Update OneDay
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
    public ResponseEntity<Message> deleteOneDay(Long userId, Long oneDayId) {
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        checkOnedayOwnership(userId, oneDay);
        oneDay.setDeleted(true);
        return ResponseEntity.ok(new Message("Deleting Complete"));

    }

    // Attending OneDay
    public ResponseEntity<?> joinOneDay(Long oneDayId, User user) {
        if (attendantRepository.existsByOneDayIdAndUserId(oneDayId, user.getId())) {
            return new ResponseEntity<>(new Message("Cannot Attend Twice"), HttpStatus.BAD_REQUEST);
        }
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        User owner = userRepository.findById(oneDay.getOwnerId()).orElseThrow(()->new NullPointerException("Owner Not Found"));
        if (Objects.equals(user.getId(), oneDay.getOwnerId())) {
            OneDayAttendant attendant = new OneDayAttendant(oneDay, user.getId());
            attendantRepository.save(attendant);
            chatService.joinChat(oneDayId, ChatTypeEnum.ONEDAY, user);
            oneDay.addAttendantNum();
            return new ResponseEntity<>("Attending Success", HttpStatus.OK);
        }
        if (oneDay.getOneDayStartTime().isBefore(timeNow)) return new ResponseEntity<>("This OneDay Is Expired", HttpStatus.BAD_REQUEST);
        if (oneDay.getType() == OneDayTypeEnum.APPROVAL) { // Logic should be added if user is owner
            // Double Check
            if (approvalRepository.existsByOneDayIdAndUserId(oneDayId, user.getId())) {
                throw new RuntimeException("Already send join");
            }
            // user is not owner
            Alert alert = Alert.builder()
                    .sender(user.getName())
//                    .imgUrl(user.getProfileImage())
                    .message(user.getNickname() + " 님이 " +oneDay.getOneDayTitle()+ "에 참여를 신청하였습니다.")
                    .receiver(owner.getNickname())
                    .check(false)
                    .build();
            alertRepository.save(alert);
            alertService.alertEvent(owner.getNickname());
            // OneDayApproval Add Process
            OneDayApproval oneDayApproval = new OneDayApproval(oneDay, user.getId());
            approvalRepository.save(oneDayApproval);
            return new ResponseEntity<>("Approval System Testing", HttpStatus.OK);
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
    public ResponseEntity<?> cancelOneDay(Long oneDayId, User user) {
        log.info("cancel process");
        OneDay oneday = loadExistingOnedayById(oneDayId);
        log.info("Attendant find");
        OneDayAttendant oneDayAttendant = findAndLoadOnedayAttendant(oneDayId, user.getId());
        int check =0;
        if (Objects.equals(oneday.getOwnerId(), oneDayAttendant.getUserId())) {
            log.info("you are owner");
            check = 1;
        }
        attendantRepository.delete(oneDayAttendant);
        chatService.leaveChat(oneDayId, ChatTypeEnum.ONEDAY, user);
        oneday.minusAttendantNum();
        if (check == 1) {
            log.info("if owner cancel attending, then oneDay should be deleted");
            oneday.setDeleted(true);
        }
        return new ResponseEntity<>("cancel attending completed", HttpStatus.OK);
    }

    // Ban Person at OneDay
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
    // Approval WaitingList
    public ResponseEntity<?> joinWishList(Long oneDayId, User user) {
        log.info("Get OneDay");
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        ArrayList<MemberResponse> approvalUsers = new ArrayList<>();
        // valid Check
        log.info("Valid Check : Are You Owner?");
        if (!Objects.equals(oneDay.getOwnerId(), user.getId())) return new ResponseEntity<>("You are Not owner", HttpStatus.FORBIDDEN);
        log.info("Get All Attendant Wish list");
        List<OneDayApproval> approvalList = approvalRepository.findAllByOneDayId(oneDayId);
        log.info("Transform userId to User Information");
        for (OneDayApproval approval : approvalList) {
            log.info("attendant get!");
            User attendant = userRepository.findById(approval.getUserId()).orElseThrow(()-> new NullPointerException("No Such User Want To Attend"));
            MemberResponse attendantInfo = new MemberResponse(attendant.getId(), attendant.getNickname(), attendant.getProfileImage());
            approvalUsers.add(attendantInfo);
        }
        return new ResponseEntity<>(approvalUsers, HttpStatus.OK);
    }
    public ResponseEntity<?> approveJoin(Long oneDayId, Long userId, User user) {
        log.info("Get Approval And OneDay");
        OneDayApproval oneDayApproval = approvalRepository.findByOneDayId(oneDayId);
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        log.info("Valid Check : Are You Owner");
        if (!Objects.equals(user.getId(), oneDay.getOwnerId())) return new ResponseEntity<>("You Are Not The Owner", HttpStatus.UNAUTHORIZED);
        // Join Process
        log.info("Valid Check : Is It Fully Occupied?");
        Integer groupSize = oneDay.getOneDayGroupSize();
        Integer currentSize = attendantRepository.findAllByOneDayId(oneDayId).size();
        // Vacant Room for attend?
        log.info("Yes, Fully Occupied");
        if (groupSize < currentSize) return new ResponseEntity<>("Fully Occupied", HttpStatus.FORBIDDEN);
        log.info("No, Vacant Area Exists");
        OneDayAttendant attendant = new OneDayAttendant(oneDay, userId);
        log.info("Add Attendant");
        attendantRepository.save(attendant);
        log.info("Invite to ChatRoom");
        chatService.joinChat(oneDayId, ChatTypeEnum.ONEDAY, user);
        log.info("AttendantsNum++");
        oneDay.addAttendantNum();
        log.info("After Join, Delete waitingList");
        approvalRepository.delete(oneDayApproval);
        return new ResponseEntity<>("Attending Success", HttpStatus.OK);
    }
    public ResponseEntity<?> rejectJoin(Long oneDayId, Long userId, User user) {
        OneDayApproval oneDayApproval = approvalRepository.findByOneDayIdAndUserId(oneDayId, userId);
        OneDay oneDay = loadExistingOnedayById(oneDayId);
        log.info("Valid Check : Are You Owner");
        if (user.getId() != oneDay.getOwnerId()) return new ResponseEntity<>("You Are Not The Owner", HttpStatus.UNAUTHORIZED);
        log.info("RejectProcess : delete from wishList");
        approvalRepository.delete(oneDayApproval);
        return new ResponseEntity<>("Reject Success", HttpStatus.OK);
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

    @Transactional
    public ResponseEntity<Message> likeOneday(Long onedayId, User user) {
        OneDay oneday = loadExistingOnedayById(onedayId);
        ResponseEntity<Message> likeServiceResponse = likeService.onedayLike(user.getId(), onedayId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        oneday.addLike();
        return likeServiceResponse;
    }

    @Transactional
    public ResponseEntity<Message> cancelLikeOneday(Long onedayId, User user) {
        OneDay oneday = loadExistingOnedayById(onedayId);
        ResponseEntity<Message> likeServiceResponse = likeService.cancelOnedayLike(user.getId(), onedayId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        oneday.minusLike();
        return likeServiceResponse;

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

    public void checkValidity(User user, Long identifier) {
        if(!attendantRepository.existsByOneDayIdAndUserId(identifier, user.getId())){
            throw new NullPointerException("Oneday join entry not found");
        }
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
