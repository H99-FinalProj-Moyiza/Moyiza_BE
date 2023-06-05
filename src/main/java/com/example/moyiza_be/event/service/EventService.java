package com.example.moyiza_be.event.service;


import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.event.dto.EventAttendantResponseDto;
import com.example.moyiza_be.event.dto.EventDetailResponseDto;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.event.dto.EventUpdateRequestDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import com.example.moyiza_be.event.repository.EventAttendantRepository;
import com.example.moyiza_be.event.repository.EventRepository;
import com.example.moyiza_be.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ClubRepository clubRepository;
    private final EventAttendantRepository attendantRepository;

    // 이벤트 생성
    @Transactional
    public ResponseEntity<?> createEvent (EventRequestDto eventRequestDto, User user, Long clubId) {
        // 클럽이 유효한가
        Club club = clubRepository.findById(clubId).orElseThrow(()-> new IllegalArgumentException("404 Not Found"));
        // 작성자가 소유자인가
        if (!user.getId().equals(club.getOwnerId())) {
            throw new IllegalArgumentException("401 UnAuthorized");
        }
        // 생성 + 삭제상태 : false + 참석자수 : 1(방장) | 참석자에 방장이 반드시 포함되어야 하는가 ? attendant 추가 : nothing change
        Event event = new Event(eventRequestDto, user.getId(), clubId); // 이미지 넣으면 user, image로 변경
        event.setDeleted(false);
//        event.setAttendantsNum(1);
        eventRepository.saveAndFlush(event);
        return new ResponseEntity<>("생성 성공", HttpStatus.OK);
    }

    // 이벤트 수정 : 보류긴 한데...
    @Transactional
    public ResponseEntity<?> updateEvent(long id, EventUpdateRequestDto requestDto, User user) throws IOException {
        // 이벤트 가져오기
        Event event = eventRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("404 Not Found"));
        // 시간 처리 어떻게 해야 하는가?
        // 존재하는 글인가? 삭제 처리하는 Entity 값 추가 필요성 논의
//        if (event.isDeleted()) {
//            throw new IllegalArgumentException("404 Not Found");
//        }
        // 이미지?
//        String image = null;
//        if (!Objects.isNull(requestDto.getImage()) && !requestDto.getImage().isEmpty() && !requestDto.getImage().getContentType().isEmpty()) {
//            image = s3Uploader.upload(requestDto.getImage(), "image");
//        }
        // 작성자인가?
//        if (Objects.equals(user.getId(), event.getUser().getId())) {
//            event.updateAll(requestDto);
//            removeCache(event);
//        } else {
//            throw new IllegalArgumentException("401 UnAuthorized");
//        }
        // 작성자이면 수정
        // 아니면 PASS
        return new ResponseEntity<>("수정 성공", HttpStatus.OK);
    }

    // 이벤트 조회
    @Transactional
    public ResponseEntity<List<EventAttendant>> getEvent(long clubId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(()->new IllegalArgumentException("400 Bad Request"));
        // 참석한 사람들
        List<EventAttendant> attendantList = attendantRepository.findByEventId(eventId);
        EventDetailResponseDto detailResponseDto = new EventDetailResponseDto(event, attendantList, attendantList.size());
        return new ResponseEntity(detailResponseDto, HttpStatus.OK);
    }

    // 전체 이벤트 조회 : 보류긴 한데
    public List<Event> getEventList(long clubId) { //ResponseEntity GenericType ListEntity
        List<Event> eventList = eventRepository.findAllByClubId(clubId);
        return eventList;
    }

    // 이벤트 삭제
    @Transactional
    public ResponseEntity<?> deleteEvent(long clubId, long eventId, User user) {
//        User user = SecurityUtil.getCurrentUser(); 이방식 말고 일단은 AuthPrincipal로 먼저
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("404 event Not found"));
        if (event.isDeleted()) { // 삭제를 T?F로 처리하면 좋을것 같은데...?
            throw new IllegalArgumentException("404 event not found");
        }
        // 만료처리도 추가해야함.
        if (user.getId().equals(event.getOwnerId())) {
            event.setDeleted(true);
            eventRepository.deleteById(eventId);
        } else {
            throw new IllegalArgumentException("401 Not Authorized");
        }
        return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    }

    // 이벤트 참석 / 취소

    public ResponseEntity<?> joinEvent(Long eventId, User user) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("404 EventNot Found"));
        if(attendantRepository.findByEventIdAndUserId(eventId, user.getId()) != null) {
            return new ResponseEntity<>(new Message("중복 가입 불가"), HttpStatus.FORBIDDEN);
        }
        EventAttendant eventAttendant = new EventAttendant(eventId, user.getId(), user);
        attendantRepository.save(eventAttendant);
        return ResponseEntity.ok("참석되었습니다.");
    }
    public ResponseEntity<?> cancelEvent(Long eventId, User user) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NullPointerException("404 Event NotFound"));
        EventAttendant eventAttendant = attendantRepository.findByEventIdAndUserId(eventId, user.getId());
        if (eventAttendant != null) {
            attendantRepository.delete(eventAttendant);
            return ResponseEntity.ok("취소되었습니다.");
        } else {
            return ResponseEntity.ok("참석권한이 없습니다.");
        }
    }


}
