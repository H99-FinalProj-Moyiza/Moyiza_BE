package com.example.moyiza_be.domain.chat.repository.QueryDSL;


import com.example.moyiza_be.domain.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.chat.dto.QChatMessageOutput;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import static com.example.moyiza_be.chat.entity.QChatRecord.chatRecord;
import static com.example.moyiza_be.user.entity.QUser.user;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRecordRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ChatMessageOutput> getPreviousChat(Pageable pageable, Long chatId) {
        return jpaQueryFactory
                .from(chatRecord)
                .join(user).on(chatRecord.senderId.eq(user.id))
                .select(
                        new QChatMessageOutput(
                                chatRecord.chatId,
                                chatRecord.id,
                                chatRecord.senderId,
                                user.nickname,
                                user.profileImage,
                                chatRecord.content,
                                chatRecord.createdAt,
                                chatRecord.modifiedAt
                        )
                )
                .where(chatRecord.chatId.eq(chatId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(chatRecord.id.desc())
                .fetch();

    }
}
