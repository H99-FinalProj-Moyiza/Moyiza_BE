package com.example.moyiza_be.alert.service;

import com.example.moyiza_be.alert.dto.AlertResponseDto;
import com.example.moyiza_be.alert.entity.Alert;
import com.example.moyiza_be.alert.repository.AlertRepository;
import com.example.moyiza_be.user.entity.User;
import com.example.moyiza_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.moyiza_be.alert.controller.SseController.sseEmitters;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    public ResponseEntity<String> alertEvent(String nickname) {
        User user = userRepository.findByNickname(nickname).get();
        String userNickName = user.getNickname();

        if (sseEmitters.containsKey(userNickName)) {
            SseEmitter sseEmitter = sseEmitters.get(userNickName);
            try {
                List<Alert> alerts = alertRepository.findAllByReceiver(user.getName());
                for (Alert alert : alerts
                ) {
                    if (!alert.isChecking()) {
                        sseEmitter.send(SseEmitter.event().data(alert));
                    }
                }
            } catch (Exception e) {
                sseEmitters.remove(userNickName);
            }
        }
        return new ResponseEntity<>("Alert Sent Complete", HttpStatus.OK);
    }

    // Read Alert on DB
    public ResponseEntity<List<Alert>> getAllAlert(User user) {
        System.out.println("alert intro");
        List<AlertResponseDto> alertList = new ArrayList<>();
        System.out.println("get all alerts By Receiver");
        List<Alert> alerts = alertRepository.findAllByReceiver(user.getName());
        System.out.println("put alerts in list");
        for (Alert alert : alerts) {
            alertList.add(
                    AlertResponseDto.builder()
                            .id(alert.getId())
                            .checking(alert.isChecking())
                            .message(alert.getMessage())
                            .receiver(alert.getReceiver())
                            .build()
            );
        }

        return new ResponseEntity<>(
                alertRepository.findAllByReceiver(user.getNickname()),HttpStatus.OK
        );
    }

    // If Alert Checked => Change to true
    public ResponseEntity<String> alertCheck(Long alertId) {
        Alert alert = alertRepository.findById(alertId).get();
        alert.setChecking(true);
        alertRepository.save(alert);
        return new ResponseEntity<>("Alert Checked",HttpStatus.OK);
    }

    // alert Subscribe
    public SseEmitter subscribe(User user) {
        // Get User
        if(user == null) throw new NullPointerException("404 User Not Found");
        Long userId = user.getId();
        // Create sseEmitter include valid time
        SseEmitter sseEmitter = new SseEmitter();

        try {
            sseEmitter.send(SseEmitter.event())
                    .name("sse")
                    .data("Alert Connected");
        } catch (IOException exception) {
            // remove if error occur
            sseEmitters.remove(userId);
            throw new IllegalArgumentException("sse Error Occur");
        }
        // save userId as key, save sseEmitter as value.
        sseEmitters.put(user.getName(), sseEmitter);
        // if async not work -> remove sseEmitter
        sseEmitter.onCompletion(()-> sseEmitters.remove(userId));
        sseEmitter.onTimeout(()->sseEmitters.remove(userId));

        return sseEmitter;
    }

    public void sendToClient(Long receiverId, Object data) {
        if (sseEmitters.containsKey(receiverId)) {
            SseEmitter sseEmitter = sseEmitters.get(receiverId);
            try {
                sseEmitter.send(SseEmitter.event()
                        .name("alert") // event name
                        .data(data)); // event Object, Contain content
            } catch (IOException exception) {
                sseEmitters.remove(receiverId); // error occur -> event delete
                throw new RuntimeException("sse error!!");
            }
        }
    }
}