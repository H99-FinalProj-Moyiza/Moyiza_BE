package com.example.moyiza_be.event.service;


import com.example.moyiza_be.event.dto.EventAttendantResponseDto;
import com.example.moyiza_be.event.dto.EventCreateResponseDto;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.event.dto.EventUpdateRequestDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import com.example.moyiza_be.event.repository.EventAttendantRepository;
import com.example.moyiza_be.event.repository.EventRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventAttendantRepository attendantRepository;

    // 이벤트 생성
    @Transactional
    public EventCreateResponseDto createEvent (EventRequestDto eventRequestDto, User user) {
        // 유저 확인
//        User user = SecurityUtil.getCurrentUser();
//        forLoggedUser(user);
        // 이미지 필요하지 않나?
//        String image = null;
        // if 문 위치 for image
        Event event = new Event(eventRequestDto, user); // 이미지 넣으면 user, image로 변경
        eventRepository.saveAndFlush(event);

        // 방장 자리 넣어야지
//        Top top = new Top(event, user);
//        topRepository.save(top);
        // 글 작성자 팔로워에게 알리는 코드 위치 (alarmService)

        return new EventCreateResponseDto(event);
    }

    // 이벤트 수정 : 보류긴 한데...
    @Transactional
    public void updateEvent(long id, EventUpdateRequestDto requestDto, User user) throws IOException {
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
    }

    // 이벤트 조회
    @Transactional
    public Optional<Event> getEvent(long clubId, long eventId) {
//        User user = Security.getCurrentUser();
        Optional<Event> eventDetailResponseDto = eventRepository.findById(eventId);
        // 있는 모임인가?
        if (eventDetailResponseDto.isEmpty()) throw new IllegalArgumentException("400 Bad Request");
        return eventDetailResponseDto;
    }

    // 전체 이벤트 조회 : 보류긴 한데
    public List<Event> getEventList(long clubId) {
        List<Event> eventList = eventRepository.findAllByClubId(clubId);
        return eventList;
    }

    // 이벤트 삭제
    @Transactional
    public void deleteEvent(long clubId, long eventId, User user) {
//        User user = SecurityUtil.getCurrentUser(); 이방식 말고 일단은 AuthPrincipal로 먼저
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("404 event Not found"));
//        if (event.isDeleted()) { // 삭제를 T?F로 처리하면 좋을것 같은데...?
//            throw new IllegalArgumentException("404 not found");
//        }
        if (user.getId().equals(event.getOwnerId())) {
            eventRepository.deleteById(eventId);
        } else {
            throw new IllegalArgumentException("401 Not Authorized");
        }
    }

    // 이벤트 참석 / 취소
    @Transactional
    public EventAttendantResponseDto addAttendant(long eventId, User user) {
        if (user == null) throw new IllegalArgumentException("401 UnAuthorized");

        Event event = (Event) eventRepository.findByIdAndDeletedIsFalse(eventId).orElseThrow(
                () -> new IllegalArgumentException("404 Not Found")
        );
        // 참석취소자가 방장일경우 참석취소 불가
        if(event.getOwnerId().equals(user)){
            throw new IllegalArgumentException("방장은 취소가 불가능해요 ㅠ.ㅠ");
        }
        // 참석자테이블에 존재하는가
        EventAttendant attendant = attendantRepository.findByEventAndUser(event, user).orElseGet(attendant);

        if (attendant == null) {
            // 최대정원 도달시 참석불가
            if (event.getEventGroupsize() <= event.getAttendantsNum()) {
                throw new IllegalArgumentException("Fully Occupied");
            }
            // 참석하지 않은 유저인 경우 참석으로 하고 참석자수++
            EventAttendant eventAttendant = attendantRepository.save(new EventAttendant(event, user));
            event.addAttend();
            return new EventAttendantResponseDto(eventAttendant);
        } else {
            // 기존에 참석했던 유저의 경우 참석자 명단에서 삭제하고 참석자수--
            attendantRepository.delete(attendant);
            event.cancelAttend();
            return null;
        }
    }

}
