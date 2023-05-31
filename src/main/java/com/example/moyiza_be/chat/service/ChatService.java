package com.example.moyiza_be.chat.service;


import com.example.moyiza_be.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessageSendingOperations sendingOperations;

    public void sendClubChat(Long clubId, ChatMessageDto chatMessageInput) {
        sendingOperations.convertAndSend("/chat/clubchat/" + clubId, chatMessageInput);
    }

    public void sendOneDayChat(Long onedayId, ChatMessageDto chatMessageInput) {
        sendingOperations.convertAndSend("/chat/onedaychat/" + onedayId, chatMessageInput);
    }


}
