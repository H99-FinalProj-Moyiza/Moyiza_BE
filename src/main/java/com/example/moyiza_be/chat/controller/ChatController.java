package com.example.moyiza_be.chat.controller;

import com.example.moyiza_be.chat.dto.*;
import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;


    //채팅 메시지 전송, 수신
    @MessageMapping("/chat/{chatId}")
    public void receiveAndSendChat(
            @DestinationVariable Long chatId, ChatMessageInput chatMessageInput,
            Message<?> message
     ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        ChatUserPrincipal userInfo = (ChatUserPrincipal) headerAccessor.getUser();

        chatService.receiveAndSendChat(userInfo, chatId, chatMessageInput);
    }

    //채팅방 목록 조회
    @GetMapping("/chat")
    public ResponseEntity<List<ChatRoomInfo>> getChatRoomList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.getChatRoomList(userDetails.getUser());
    }

    //채팅 내역 조회
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Page<ChatRecordDto>> getChatRecordList(
            @PageableDefault(page = 0, size = 50, sort = "CreatedAt", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable Long chatId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return chatService.getChatRoomRecord(userDetails.getUser(), chatId, pageable);
    }

}
