package com.example.moyiza_be.chat.service;

import com.example.moyiza_be.chat.dto.*;
import com.example.moyiza_be.chat.entity.Chat;
import com.example.moyiza_be.chat.entity.ChatJoinEntry;
import com.example.moyiza_be.chat.entity.ChatRecord;
import com.example.moyiza_be.chat.repository.ChatJoinEntryRepository;
import com.example.moyiza_be.chat.repository.ChatRecordRepository;
import com.example.moyiza_be.chat.repository.ChatRepository;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional  // transaction 때문에 느려질 수 있다
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRecordRepository chatRecordRepository;
    private final ChatJoinEntryRepository chatJoinEntryRepository;
    private final ChatRepository chatRepository;

    public void receiveAndSendChat(ChatUserPrincipal userPrincipal, Long chatId, ChatMessageInput chatMessageInput) {
        //필터링 ? some logic
        ChatRecord chatRecord = chatMessageInput.toChatRecord(chatId, userPrincipal.getUserId());
        chatRecordRepository.save(chatRecord);  // id받아오려면 saveAndFlush로 변경
        String destination = "/chat/" + chatId;
        ChatMessageOutput messageOutput = new ChatMessageOutput(chatRecord, userPrincipal);
        sendingOperations.convertAndSend(destination, messageOutput);
    }


    //채팅방 목록 조회
    public ResponseEntity<List<ChatRoomInfo>> getChatRoomList(User user) {
        //나중에 쿼리 바꿀 대상

        List<Long> joinedChatIdList = chatJoinEntryRepository.findAllByUserIdAndIsCurrentlyJoinedTrue(user.getId())
                .stream()
                .map(ChatJoinEntry::getChatId)
                .toList();  //Jpa에서는 다른 엔티티끼리 join 안됨
        List<ChatRoomInfo> chatRoomInfoList = chatRepository.findAllByIdIn(joinedChatIdList)
                                    .stream()
                                    .map(ChatRoomInfo::new)
                                    .toList();
        return ResponseEntity.ok(chatRoomInfoList);
    }

    public void makeChat(Long roomIdentifier, ChatTypeEnum chatType, String roomName){
        Chat chat = chatRepository.findByRoomIdentifierAndChatType(roomIdentifier, chatType).orElse(null);
        if(chat != null){
            log.info("chat room already exists by id = " + chat.getId());
            throw new NullPointerException("chat room already exists by id = " + chat.getId());
        }
        Chat new_chat = new Chat(roomIdentifier,chatType, roomName);
        chatRepository.save(new_chat);
    }

    //채팅 내역 조회
    public ResponseEntity<Page<ChatRecordDto>> getChatRoomRecord(User user, Long chatId, Pageable pageable){
        //나중에 쿼리 다듬기
        ChatJoinEntry chatJoinEntry =
                chatJoinEntryRepository.findByUserIdAndChatIdAndIsCurrentlyJoinedTrue(user.getId(), chatId)
                .orElse(null);

        if(chatJoinEntry == null){ throw new NullPointerException("채팅방을 찾을 수 없습니다"); }

        Page<ChatRecordDto> chatRecordDtoPage = chatRecordRepository.findAllByChatIdAndCreatedAtAfter
                                                (pageable, chatId, chatJoinEntry.getModifiedAt())
                                                    .map(ChatRecordDto::new);
        return ResponseEntity.ok(chatRecordDtoPage);
    }

    //클럽 채팅방 join
    public void joinChat(Long roomIdentifier, ChatTypeEnum chatType, User user) {
        Chat chat = loadChat(roomIdentifier,chatType);
        ChatJoinEntry chatJoinEntry = chatJoinEntryRepository.findByChatIdAndUserId(chat.getId(), user.getId()).orElse(null);

        if (chatJoinEntry == null) {
            chatJoinEntryRepository.save(new ChatJoinEntry(chat.getId(), user.getId()));
        } else {
            chatJoinEntry.setCurrentlyJoined(true);
        }

        //구독자들한테 JOIN메시지 보내기
        ChatUserPrincipal adminInfo = new ChatUserPrincipal(-1L, "admin", "adminProfileImage");
        receiveAndSendChat(adminInfo, chat.getId(), new ChatMessageInput(user.getNickname() + "님이 참여했습니다"));

    }

    public void leaveChat(Long roomIdentifier, ChatTypeEnum chatType, User user){
        Chat chat = loadChat(roomIdentifier,chatType);
        ChatJoinEntry chatJoinEntry = chatJoinEntryRepository.findByChatIdAndUserId(chat.getId(), user.getId()).orElse(null);

        if (chatJoinEntry == null){
            throw new NullPointerException("채팅 참여정보를 찾을 수 없습니다");
        }
        else{
            chatJoinEntry.setCurrentlyJoined(false);
        }

        //구독자들한테 LEAVE메시지 보내기
        ChatUserPrincipal adminInfo = new ChatUserPrincipal(-1L, "admin", "asdf");
        receiveAndSendChat(adminInfo, chat.getId(), new ChatMessageInput(user.getNickname() + "님이 나가셨습니다"));
    }

    private Chat loadChat(Long roomIdentifier, ChatTypeEnum chatTypeEnum){
        return chatRepository.findByRoomIdentifierAndChatType(roomIdentifier, chatTypeEnum)
                .orElseThrow(() -> new NullPointerException("채팅방을 찾을 수 없습니다"));

    }

}
