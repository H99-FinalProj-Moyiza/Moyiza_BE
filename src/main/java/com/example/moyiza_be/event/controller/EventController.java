package com.example.moyiza_be.event.controller;

import com.example.moyiza_be.common.security.UserDetailsImpl;
import com.example.moyiza_be.event.dto.DataResponseDto;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.event.dto.ResponseDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class EventController {
    private final EventService eventService;

    // create
    @PostMapping("/{club_id}/event")
    public ResponseDto createEvent(@RequestBody EventRequestDto eventRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.of(eventService.createEvent(eventRequestDto, userDetails.getUser()));
    }

    // ReadAll
    // url없음  : 내부적으로 처리

    // ReadOne
    @GetMapping("/{club_id}/event/{event_id}")
    public DataResponseDto<Optional<Event>> getEvent(@PathVariable long id) {
        return DataResponseDto.of(eventService.getEvent(id));
    }

    // Update : 보류
//    @PutMapping("/{club_id}/event/{event_id}")
//    public String putEvent() {
//
//    }
    // Delete
    @DeleteMapping("/{club_id}/event/{event_id}")
    public String deleteEvent(@PathVariable long id) {
        eventService.deleteEvent(id);
        return "삭제되었습니다.";
    }
    // Attend
//    @PostMapping("/{club_id}/event/join/{event_id}")
//    public String attendEvent() {
//
//    }
    // Cancel Attend

}
