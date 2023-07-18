package com.example.moyiza_be.common.common_features.alert.controller;

import com.example.moyiza_be.common.common_features.alert.service.AlertService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/alert")
public class SseController {

    public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final JwtUtil jwtUtil;
    public final AlertService alertService;

    @CrossOrigin
    @GetMapping(value = "/sub", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String token) {
        // Parsing User's PK value from Token
        String userName = jwtUtil.getUserNameFromToken(token);

        // Create SseEmitter for Current Client
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            // Connect
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save SseEmitter setting user's pk as key value
        sseEmitters.put(userName, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(userName));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userName));
        sseEmitter.onError((e) -> sseEmitters.remove(userName));

        alertService.alertEvent(userName);
        return sseEmitter;
    }
}