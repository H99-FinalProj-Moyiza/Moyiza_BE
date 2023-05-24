package com.example.moyiza_be.event.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.event.dto.DataResponseDto;
import com.example.moyiza_be.event.dto.EventCreateResponseDto;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.event.dto.ResponseDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> createEvent(@RequestBody EventRequestDto eventRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return eventService.createEvent(eventRequestDto, userDetails.getUser());
    }

    // ReadAll
    // url없음  : 내부적으로 처리

    // ReadOne
    @GetMapping("/{club_id}/event/{event_id}")
    public DataResponseDto<Optional<Event>> getEvent(@PathVariable long club_id, @PathVariable long event_id) {
        return DataResponseDto.of(eventService.getEvent(club_id,event_id), "조회 성공");
    }

    // Update : 보류
//    @PutMapping("/{club_id}/event/{event_id}")
//    public String putEvent() {
//    }

    // Delete
    @DeleteMapping("/{club_id}/event/{event_id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long club_id, @PathVariable long event_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return eventService.deleteEvent(club_id,event_id, userDetails.getUser());
    }

    // Attend
//    @PostMapping("/{club_id}/event/join/{event_id}")
//    public String attendEvent(@PathVariable long club_id, @PathVariable long event_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//    }

    // Cancel Attend
//    @DeleteMapping("/{club_id}/event/join/{event_id}")
//    public String attendEvent(@PathVariable long club_id, @PathVariable long event_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//
//    }
}
