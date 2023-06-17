package com.example.moyiza_be.chat.service;

import com.example.moyiza_be.chat.dto.*;
import com.example.moyiza_be.chat.entity.Chat;
import com.example.moyiza_be.chat.entity.ChatJoinEntry;
import com.example.moyiza_be.chat.entity.ChatRecord;
import com.example.moyiza_be.chat.repository.ChatJoinEntryRepository;
import com.example.moyiza_be.chat.repository.ChatRecordRepository;
import com.example.moyiza_be.chat.repository.ChatRepository;
import com.example.moyiza_be.chat.repository.QueryDSL.ChatRecordRepositoryCustom;
import com.example.moyiza_be.chat.repository.QueryDSL.ChatRepositoryCustom;
import com.example.moyiza_be.club.entity.ClubImageUrl;
import com.example.moyiza_be.club.repository.ClubImageUrlRepository;
import com.example.moyiza_be.club.repository.ClubRepository;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.example.moyiza_be.common.redis.RedisCacheService;
import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import com.example.moyiza_be.oneday.repository.OneDayImageUrlRepository;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRecordRepository chatRecordRepository;
    private final ChatJoinEntryRepository chatJoinEntryRepository;
    private final ChatRepository chatRepository;
    private final RedisCacheService cacheService;
    private final ChatRepositoryCustom chatRepositoryCustom;
    private final ChatRecordRepositoryCustom chatRecordRepositoryCustom;
    private final ClubImageUrlRepository clubImageUrlRepository;
    private final OneDayImageUrlRepository oneDayImageUrlRepository;
    private final String DEFAULT_CHATROOM_IMAGE = "https://moyiza-image.s3.ap-northeast-2.amazonaws.com/e048a389-8488-4ab5-a973-c8e5bae99d2e_pngaaa.com-293633.png";

    public void receiveAndSendChat(ChatUserPrincipal userPrincipal,
                                   Long chatId,
                                   ChatMessageInput chatMessageInput
    ) {
        //Filtering ? need to some logic
        ChatRecord chatRecord = chatMessageInput.toChatRecord(chatId, userPrincipal.getUserId());
        chatRecordRepository.save(chatRecord);  // id받아오려면 saveAndFlush로 변경
        Long subscriptionCount = cacheService.countSubscriptionToChatId(chatId.toString());
        Long chatMemberCount = getChatMemberCount(chatId);
        ChatMessageOutput messageOutput = new ChatMessageOutput(chatRecord, userPrincipal, chatMemberCount - subscriptionCount);
        messageOutput.setChatId(chatId);
        String destination = "/chat/" + chatId;
        String alarmDestination = "/chatalarm/" + chatId;
        cacheService.addRecentChatToList(chatId.toString(), messageOutput);
        sendingOperations.convertAndSend(destination, messageOutput);
        sendingOperations.convertAndSend(alarmDestination, messageOutput);
    }

    //Get Club Chat Room List
    public ResponseEntity<List<ChatRoomInfo>> getClubChatRoomList(Long userId) {
        //나중에 쿼리 바꿀 대상
        List<ChatRoomInfo> clubChatRoomInfoList = chatRepositoryCustom.getClubChatRoomList(userId)
                .stream()
                .peek(chatRoomInfo ->
                        chatRoomInfo.setLastMessage(
                                cacheService.loadRecentChat(chatRoomInfo.getChatId().toString())))
                .peek(chatRoomInfo ->
                        chatRoomInfo.setChatThumbnail(
                                findClubThumbnailUrl(chatRoomInfo.getRoomIdentifier())
                        )
                )
                .toList();

        return ResponseEntity.ok(clubChatRoomInfoList);
    }

    public ResponseEntity<List<ChatRoomInfo>> getOnedayChatRoomList(Long userId) {

        List<ChatRoomInfo> onedayChatRoomInfoList = chatRepositoryCustom.getOnedayChatRoomList(userId)
                .stream()
                .peek(chatRoomInfo ->
                        chatRoomInfo.setLastMessage(
                                cacheService.loadRecentChat(chatRoomInfo.getChatId().toString())))
                .peek(chatRoomInfo ->
                        chatRoomInfo.setChatThumbnail(
                                findOnedayThumbnailUrl(chatRoomInfo.getRoomIdentifier())
                        )
                )
                .toList();
        return ResponseEntity.ok(onedayChatRoomInfoList);
    }

    public void makeChat(Long roomIdentifier, ChatTypeEnum chatType, String roomName) {
        Chat chat = chatRepository.findByRoomIdentifierAndChatType(roomIdentifier, chatType).orElse(null);
        if (chat != null) {
            log.info("chat room already exists by id = " + chat.getId());
            throw new NullPointerException("chat room already exists by id = " + chat.getId());
        }
        Chat new_chat = new Chat(roomIdentifier, chatType, roomName);
        chatRepository.saveAndFlush(new_chat);
    }

    // Get Chat Room Record
    public ResponseEntity<Page<ChatMessageOutput>> getChatRoomRecord(User user, Long chatId, Pageable pageable) {
        ChatJoinEntry chatJoinEntry =
                chatJoinEntryRepository.findByUserIdAndChatIdAndIsCurrentlyJoinedTrue(user.getId(), chatId)
                        .orElseThrow(() -> new NullPointerException("chatRoomEntry not found"));
        Long memberCount = getChatMemberCount(chatId);
        if (pageable.getPageNumber() == 0) {
            return ResponseEntity.ok(cacheService.loadRecentChatList(chatId.toString(), memberCount, pageable));
        }
        List<ChatMessageOutput> previousChatList = chatRecordRepositoryCustom.getPreviousChat(pageable, chatId);

        if (previousChatList == null) {
            return ResponseEntity.ok(new PageImpl<>(new LinkedList<>(), pageable, 0L));
        } else {
            for (ChatMessageOutput chatMessage : previousChatList) {
                chatMessage.setUnreadCount(
                        memberCount - cacheService.getTotalReadCount(chatId.toString(), chatMessage.getChatRecordId()));
            }
        }
        Page<ChatMessageOutput> chatRecordPage = new PageImpl<>(previousChatList, pageable, 10000L);
        return ResponseEntity.ok(chatRecordPage);
    }

    //클럽 채팅방 join
    public void joinChat(Long roomIdentifier, ChatTypeEnum chatType, User user) {
        Chat chat = loadChat(roomIdentifier, chatType);
        ChatJoinEntry chatJoinEntry = chatJoinEntryRepository.findByChatIdAndUserId(chat.getId(), user.getId()).orElse(null);

        if (chatJoinEntry == null) {
            chatJoinEntryRepository.save(new ChatJoinEntry(chat.getId(), user.getId()));
        } else {
            chatJoinEntry.setCurrentlyJoined(true);
        }
        cacheService.addUnsubscribedUser(chat.getId().toString(), user.getId().toString());

        //구독자들한테 JOIN메시지 보내기
        ChatUserPrincipal adminInfo = new ChatUserPrincipal(-1L, "admin", "adminProfileImage");
        receiveAndSendChat(adminInfo, chat.getId(), new ChatMessageInput(user.getNickname() + "님이 참여했습니다"));

    }

    public void leaveChat(Long roomIdentifier, ChatTypeEnum chatType, User user) {
        Chat chat = loadChat(roomIdentifier, chatType);
        ChatJoinEntry chatJoinEntry = chatJoinEntryRepository.findByChatIdAndUserId(chat.getId(), user.getId()).orElse(null);

        if (chatJoinEntry == null) {
            throw new NullPointerException("채팅 참여정보를 찾을 수 없습니다");
        } else {
            chatJoinEntryRepository.delete(chatJoinEntry);
        }
        cacheService.removeUnsubscribedUser(chat.getId().toString(), user.getId().toString());
        cacheService.removeSubscriptionFromChatId(chat.getId().toString(), user.getId().toString());

        //구독자들한테 LEAVE메시지 보내기
        ChatUserPrincipal adminInfo = new ChatUserPrincipal(-1L, "admin", "asdf");
        receiveAndSendChat(adminInfo, chat.getId(), new ChatMessageInput(user.getNickname() + "님이 나가셨습니다"));
    }

    private Chat loadChat(Long roomIdentifier, ChatTypeEnum chatTypeEnum) {
        return chatRepository.findByRoomIdentifierAndChatType(roomIdentifier, chatTypeEnum)
                .orElseThrow(() -> new NullPointerException("채팅방을 찾을 수 없습니다"));
    }

    public Long getChatMemberCount(Long chatId) {
        return chatJoinEntryRepository.countByChatIdAndAndIsCurrentlyJoinedTrue(chatId);
    }

    public String findClubThumbnailUrl(Long clubId) {
        ClubImageUrl clubImageEntity = clubImageUrlRepository.findFirstByClubId(clubId)
                .orElse(null);
        return clubImageEntity == null ? DEFAULT_CHATROOM_IMAGE : clubImageEntity.getImageUrl();
    }

    public String findOnedayThumbnailUrl(Long onedayId) {
        OneDayImageUrl onedayImageEntity = oneDayImageUrlRepository.findFirstByOneDayId(onedayId)
                .orElse(null);
        return onedayImageEntity == null ? DEFAULT_CHATROOM_IMAGE : onedayImageEntity.getImageUrl();
    }
}
