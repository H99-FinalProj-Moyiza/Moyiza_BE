package com.example.moyiza_be.chat.controller;


import com.example.moyiza_be.chat.dto.ChatMessageDto;
import com.example.moyiza_be.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessageSendingOperations sendingOperations;


    @MessageMapping("/send/clubchat/{clubId}")
    public void sendClubChat(@DestinationVariable Long clubId, ChatMessageDto chatMessageInput) {
        chatService.sendClubChat(clubId, chatMessageInput);
    }

    @MessageMapping("/send/onedaychat/{onedayId}")
    public void sendOneDayChat(@DestinationVariable Long onedayId, ChatMessageDto chatMessageInput){;
        chatService.sendOneDayChat(onedayId, chatMessageInput);
    }
}
