package com.example.moyiza_be.chat.controller;

import com.example.moyiza_be.chat.dto.*;
import com.example.moyiza_be.chat.service.ChatService;
import com.example.moyiza_be.common.security.jwt.JwtUtil;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;


@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final JwtUtil jwtUtil;


    //채팅 메시지 전송, 수신
    @MessageMapping("/chat/{chatId}")
    public void receiveAndSendChat(
            @DestinationVariable Long chatId, ChatMessageInput chatMessageInput,
            Message<?> message
     ) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String bearerToken = String.valueOf(headerAccessor.getNativeHeader("ACCESS_TOKEN"))
                .replaceAll("[\\[\\]]","");
        if (bearerToken.equals("null")){
            throw new IllegalArgumentException("유저정보를 찾을 수 없습니다");
        }
        String token = jwtUtil.removePrefix(bearerToken);
        if (!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("토큰이 유효하지 않습니다");
        }
        Claims claims = jwtUtil.getClaimsFromToken(token);
        ChatUserPrincipal userInfo;
        try{
            userInfo = new ChatUserPrincipal(
                    Long.valueOf(claims.get("userId").toString()),
                    claims.get("nickName").toString(),
                    claims.get("profileUrl").toString()
            );
        } catch(RuntimeException e){
            log.info("채팅 : 토큰에서 유저정보를 가져올 수 없음");
            throw new NullPointerException("chat : 유저정보를 읽을 수 없습니다");
        }
        chatService.receiveAndSendChat(userInfo, chatId, chatMessageInput, null);

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
