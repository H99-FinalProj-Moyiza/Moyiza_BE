package com.example.moyiza_be.common.common_features.alert.controller;

import com.example.moyiza_be.common.common_features.alert.entity.Alert;
import com.example.moyiza_be.common.common_features.alert.service.AlertService;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/alert")
@RestController
public class AlertController {
    private final AlertService alertService;

    // Show All Alert On Owner's NickName
    @GetMapping("/alerts")
    public ResponseEntity<List<Alert>> getAllAlert(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return alertService.getAllAlert(userDetails.getUser());
    }
    @PostMapping("/{alertId}")
    public ResponseEntity<String> checkAlert(@PathVariable Long alertId) {
        return alertService.alertCheck(alertId);
    }

    // Alert Subscribe
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {return alertService.subscribe(userDetails.getUser()); }
}
