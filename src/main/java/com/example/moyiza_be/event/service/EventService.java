package com.example.moyiza_be.event.service;


import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.event.dto.EventDetailResponseDto;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.event.dto.EventSimpleDetailDto;
import com.example.moyiza_be.event.dto.EventUpdateRequestDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import com.example.moyiza_be.event.repository.EventAttendantRepository;
import com.example.moyiza_be.event.repository.EventRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final EventAttendantRepository attendantRepository;
    private final AwsS3Uploader s3Uploader;
    public static final String basicImageUrl = "https://moyiza-image.s3.ap-northeast-2.amazonaws.com/87f7fcdb-254b-474a-9bf0-86cf3e89adcc_basicProfile.jpg";

    // Create Event
    @Transactional
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
        if(!image.isEmpty()) {
            imageUrl = s3Uploader.uploadFile(image);
        }
        // Create + (Delete : false) + (AttendantsNum : 1(Owner)) | (Should Attendants contain owner? attendantsNum++ : nothing change)
        Event event = new Event(eventRequestDto, user.getId(), clubId, imageUrl); // 이미지 넣으면 user, image로 변경
        event.setDeleted(false);
//        event.setAttendantsNum(1);
        eventRepository.saveAndFlush(event);
        return new ResponseEntity<>("Create Success", HttpStatus.OK);
    }

    // Update Event : Hold
    @Transactional
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
        return new ResponseEntity<>("Update Success", HttpStatus.OK);
    }

    // Read Event Detail
    @Transactional
    public ResponseEntity<List<EventAttendant>> getEvent(long clubId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(()->new IllegalArgumentException("400 Bad Request"));
        // Attending People List
        List<EventAttendant> attendantList = attendantRepository.findByEventId(eventId);
        EventDetailResponseDto detailResponseDto = new EventDetailResponseDto(event, attendantList, attendantList.size());
        return new ResponseEntity(detailResponseDto, HttpStatus.OK);
    }

    // Event ReadAll
    public List<EventSimpleDetailDto> getEventList(long clubId) { //ResponseEntity GenericType ListEntity
        List<Event> eventsList = eventRepository.findAllByClubId(clubId);
        List<EventSimpleDetailDto> eventList = new ArrayList<>();
        for (Event event: eventsList) {
            List<EventAttendant> eventAttendantList = attendantRepository.findByEventId(event.getId());
            EventSimpleDetailDto simpleDetailDto = new EventSimpleDetailDto(event, eventAttendantList.size());
            eventList.add(simpleDetailDto);
        }
        return eventList;
    }

    // Deleting Event
    @Transactional
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
        if(attendantRepository.findByEventIdAndUserId(eventId, user.getId()) != null) {
            return new ResponseEntity<>(new Message("Cannot Attend Twice"), HttpStatus.FORBIDDEN);
        }
        EventAttendant eventAttendant = new EventAttendant(eventId, user.getId(), user);
        attendantRepository.save(eventAttendant);
        event.addAttend();
        return ResponseEntity.ok("Attending Complete.");
    }
    public ResponseEntity<?> cancelEvent(Long eventId, User user) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("404 Event NotFound"));
        EventAttendant eventAttendant = attendantRepository.findByEventIdAndUserId(eventId, user.getId());
        if (eventAttendant != null) {
            attendantRepository.delete(eventAttendant);
            event.cancelAttend();
            return ResponseEntity.ok("Cancel Complete.");
        } else {
            return ResponseEntity.ok("401 Unauthorized.");
        }
    }


}
