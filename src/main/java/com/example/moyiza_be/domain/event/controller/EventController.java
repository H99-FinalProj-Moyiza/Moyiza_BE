package com.example.moyiza_be.domain.event.controller;

import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.domain.event.dto.EventRequestDto;
import com.example.moyiza_be.domain.event.service.EventService;
import com.example.moyiza_be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class EventController {
    private final EventService eventService;

    // create
    @PostMapping(value = "/{club_id}/event", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createEvent(@RequestPart(value = "data") EventRequestDto eventRequestDto, @RequestPart(value = "image")MultipartFile image, @PathVariable Long club_id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return eventService.createEvent(eventRequestDto, userDetails.getUser(), club_id, image);
    }

    // ReadAll
    // NO url : Process in Club

    // ReadOne
    @GetMapping("/{club_id}/event/{event_id}")
    public ResponseEntity<?> getEvent(
            @PathVariable Long club_id,
            @PathVariable Long event_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails == null ? null : userDetails.getUser();
        return eventService.getEvent(club_id,event_id, user);
    }

    // Update : Hold
//    @PutMapping("/{club_id}/event/{event_id}")
//    public String putEvent() {
//    }

    // Delete
    @DeleteMapping("/{club_id}/event/{event_id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long club_id, @PathVariable long event_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return eventService.deleteEvent(club_id,event_id, userDetails.getUser());
    }

//     Attend
    @PostMapping("/{club_id}/event/join/{event_id}")
    public ResponseEntity<?> joinEvent(@PathVariable(name = "club_id") Long clubId, @PathVariable(name = "event_id") Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return eventService.joinEvent(eventId, userDetails.getUser());
    }

    // Cancel Attend
    @DeleteMapping("/{club_id}/event/join/{event_id}")
    public ResponseEntity<?> attendEvent(@PathVariable long club_id, @PathVariable long event_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return eventService.cancelEvent(event_id, userDetails.getUser());
    }

    @PostMapping("/{club_id}/event/{event_id}/like")
    public ResponseEntity<Message> likeClub(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long event_id
    ){
        User user = userDetails.getUser();
        return eventService.likeEvent(user, event_id);
    }

    @DeleteMapping("/{club_id}/event/{event_id}/like")
    public ResponseEntity<Message> cancelLikeClub(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long event_id
    ){
        User user = userDetails.getUser();
        return eventService.cancelLikeEvent(user, event_id);
    }
}
