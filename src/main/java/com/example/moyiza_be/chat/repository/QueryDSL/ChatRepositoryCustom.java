package com.example.moyiza_be.chat.repository.QueryDSL;


import com.example.moyiza_be.chat.dto.ChatRoomInfo;
import com.example.moyiza_be.chat.dto.QChatRoomInfo;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.chat.entity.QChat.chat;
import static com.example.moyiza_be.chat.entity.QChatJoinEntry.chatJoinEntry;
import static com.example.moyiza_be.club.entity.QClub.club;
import static com.example.moyiza_be.oneday.entity.QOneDay.oneDay;
import static com.example.moyiza_be.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ChatRoomInfo> getClubChatRoomList(Long userId) {
        return jpaQueryFactory
                .from(chatJoinEntry)
                .join(user).on(chatJoinEntry.userId.eq(userId))
                .join(chat).on(chatJoinEntry.chatId.eq(chat.id).and(chat.chatType.eq(ChatTypeEnum.CLUB)))
                .join(club).on(chat.roomIdentifier.eq(club.id))
                .select(
                        new QChatRoomInfo(
                                chat.id,
                                chat.chatType,
                                chat.roomIdentifier,
                                chat.roomName,
                                club.thumbnailUrl
                        )
                )
                .fetch();
    }

    public List<ChatRoomInfo> getOnedayChatRoomList(Long userId) {
        return jpaQueryFactory
                .from(chatJoinEntry)
                .join(user).on(chatJoinEntry.userId.eq(userId))
                .join(chat).on(chatJoinEntry.chatId.eq(chat.id).and(chat.chatType.eq(ChatTypeEnum.ONEDAY)))
                .join(oneDay).on(chat.roomIdentifier.eq(oneDay.id))
                .select(
                        new QChatRoomInfo(
                                chat.id,
                                chat.chatType,
                                chat.roomIdentifier,
                                chat.roomName,
                                oneDay.oneDayImage
                        )
                )
                .fetch();
    }
}
