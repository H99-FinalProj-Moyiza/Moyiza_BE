package com.example.moyiza_be.domain.event.service;


import com.example.moyiza_be.common.common_features.blackList.service.BlackListService;
import com.example.moyiza_be.domain.club.entity.Club;
import com.example.moyiza_be.domain.club.repository.ClubRepository;
import com.example.moyiza_be.common.enums.BoardTypeEnum;
import com.example.moyiza_be.common.enums.LikeTypeEnum;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.domain.event.dto.EventDetailResponseDto;
import com.example.moyiza_be.domain.event.dto.EventRequestDto;
import com.example.moyiza_be.domain.event.dto.EventSimpleDetailDto;
import com.example.moyiza_be.domain.event.dto.EventUpdateRequestDto;
import com.example.moyiza_be.domain.event.entity.Event;
import com.example.moyiza_be.domain.event.entity.EventAttendant;
import com.example.moyiza_be.domain.event.repository.EventRepository;
import com.example.moyiza_be.domain.event.repository.QueryDSL.EventRepositoryCustom;
import com.example.moyiza_be.domain.event.repository.EventAttendantRepository;
import com.example.moyiza_be.domain.like.service.LikeService;
import com.example.moyiza_be.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final EventAttendantRepository attendantRepository;
    private final AwsS3Uploader s3Uploader;
    private final LikeService likeService;
    private final EventRepositoryCustom eventRepositoryCustom;
    private final EventAttendantRepository eventAttendantRepository;
    private final BlackListService blackListService;
    public static final String basicImageUrl = "https://moyiza-image.s3.ap-northeast-2.amazonaws.com/87f7fcdb-254b-474a-9bf0-86cf3e89adcc_basicProfile.jpg";

    // Create Event
    public ResponseEntity<?> createEvent (EventRequestDto eventRequestDto, User user, Long clubId, MultipartFile image) throws IOException {
        // Is Club Valid?
        Club club = clubRepository.findById(clubId).orElseThrow(()-> new IllegalArgumentException("404 Not Found"));
        // Owner == writer??
        if (!user.getId().equals(club.getOwnerId())) {
            throw new IllegalArgumentException("401 UnAuthorized");
        }
        String imageUrl = basicImageUrl;
//        if(!Objects.isNull(eventRequestDto.getImage()) && !eventRequestDto.getImage().isEmpty() && !eventRequestDto.getImage().getContentType().isEmpty()){
//            imageUrl = s3Uploader.eventUpload(eventRequestDto.getImage(), "image");
//        }
        log.info("Is Image Empty?");
        if(!image.isEmpty()) {
            log.info("No, Image is not Empty!");
            imageUrl = s3Uploader.uploadFile(image);
        }
        log.info("create Event");
        // Create + (Delete : false) + (AttendantsNum : 1(Owner)) | (Should Attendants contain owner? attendantsNum++ : nothing change)
        Event event = new Event(eventRequestDto, user.getId(), clubId, imageUrl); // 이미지 넣으면 user, image로 변경
        log.info("set Deleted false");
        event.setDeleted(false);
//        event.setAttendantsNum(1);
        eventRepository.saveAndFlush(event);
        log.info("Create and Save Complete!");
        return new ResponseEntity<>("Create Success", HttpStatus.OK);
    }

    // Update Event : Hold
    public ResponseEntity<?> updateEvent(long id, EventUpdateRequestDto requestDto, User user) throws IOException {
        // Get Event
        Event event = eventRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("404 Not Found"));
        // Time Process?
        // Valid Post Check? Deleting Process Entity Value's Necessity Should be discussed
//        if (event.isDeleted()) {
//            throw new IllegalArgumentException("404 Not Found");
//        }
        // Image Process
//        String image = null;
//        if (!Objects.isNull(requestDto.getImage()) && !requestDto.getImage().isEmpty() && !requestDto.getImage().getContentType().isEmpty()) {
//            image = s3Uploader.upload(requestDto.getImage(), "image");
//        }
        // Author Check
//        if (Objects.equals(user.getId(), event.getUser().getId())) {
//            event.updateAll(requestDto);
//            removeCache(event);
//        } else {
//            throw new IllegalArgumentException("401 UnAuthorized");
//        }
        // If Author == user
        // Else Pass
        throw new NullPointerException("Not Implemented");
//        return new ResponseEntity<>("Update Success", HttpStatus.OK);
    }

    // Read Event Detail
    public ResponseEntity<List<EventAttendant>> getEvent(long clubId, long eventId, User user) {
        Long userId = user == null ? null : user.getId();
        Event event = eventRepository.findById(eventId).orElseThrow(()->new IllegalArgumentException("400 Bad Request"));
        // Attending People List
        List<EventAttendant> attendantList = attendantRepository.findByEventId(eventId);
        Boolean isLikedByUser = likeService.checkLikeExists(userId, eventId, LikeTypeEnum.EVENT);
        EventDetailResponseDto detailResponseDto = new EventDetailResponseDto(
                event, attendantList, attendantList.size(), isLikedByUser);
        return new ResponseEntity(detailResponseDto, HttpStatus.OK);
    }

    // Event ReadAll
    public List<EventSimpleDetailDto> getEventList(long clubId, User user) { //ResponseEntity GenericType ListEntity
        List<Long> blackEventIdList = blackListService.blackListFiltering(user, BoardTypeEnum.EVENT);
        return eventRepositoryCustom.getClubEventList(clubId, user, blackEventIdList);
    }

    // Deleting Event
    public ResponseEntity<?> deleteEvent(long clubId, long eventId, User user) {
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("404 event Not found"));
        if (event.isDeleted()) { // Deleting Process Boolean Setting?
            throw new IllegalArgumentException("404 event not found");
        }
        // Expired Should be Added.
        if (user.getId().equals(event.getOwnerId())) {
            event.setDeleted(true);
            eventRepository.deleteById(eventId);
        } else {
            throw new IllegalArgumentException("401 Not Authorized");
        }
        return new ResponseEntity<>("Delete Complete", HttpStatus.OK);
    }

    // Event Attend/Cancel
    public ResponseEntity<?> joinEvent(Long eventId, User user) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("404 EventNot Found"));
        log.info("Double Attend Check");
        if(attendantRepository.findByEventIdAndUserId(eventId, user.getId()) != null) {
            log.info("Fail! You Already Attend The Event!");
            return new ResponseEntity<>(new Message("Cannot Attend Twice"), HttpStatus.FORBIDDEN);
        }
        log.info("Add AttendantList");
        EventAttendant eventAttendant = new EventAttendant(eventId, user.getId(), user);
        attendantRepository.save(eventAttendant);
        log.info("AttendantsNum++");
        event.addAttend();
        return ResponseEntity.ok("Attending Complete.");
    }

    public ResponseEntity<?> cancelEvent(Long eventId, User user) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("404 Event NotFound"));
        log.info("Did You Attend The Event?");
        EventAttendant eventAttendant = attendantRepository.findByEventIdAndUserId(eventId, user.getId());
        if (eventAttendant != null) {
            log.info("Yes! And Want To Cancel.");
            attendantRepository.delete(eventAttendant);
            log.info("AttendantsNum--");
            event.cancelAttend();
            return ResponseEntity.ok("Cancel Complete.");
        } else {
            log.info("No, You Did Not Attend The Event!");
            return ResponseEntity.ok("401 Unauthorized.");
        }
    }


    @Transactional
    public ResponseEntity<Message> likeEvent(User user, Long eventId) {
        Event event = loadEventById(eventId);

        ResponseEntity<Message> likeServiceResponse = likeService.eventLike(user.getId(), eventId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        event.addLike();
        return likeServiceResponse;
    }

    @Transactional
    public ResponseEntity<Message> cancelLikeEvent(User user, Long eventId){
        Event event = loadEventById(eventId);
        ResponseEntity<Message> likeServiceResponse = likeService.cancelEventLike(user.getId(), eventId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        event.minusLike();
        return likeServiceResponse;
    }

    private Event loadEventById(Long eventId){
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NullPointerException("Event Not Found for eventId : " + eventId));
    }

    public void checkValidity(User user, Long identifier) {
        if (!eventAttendantRepository.existsByUserIdAndEventId(user.getId(), identifier)){
            throw new NullPointerException("Event join entry not found for userId " + user.getId() + "eventId : " + identifier);
        }
    }
}
