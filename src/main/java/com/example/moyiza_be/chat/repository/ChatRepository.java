package com.example.moyiza_be.chat.repository;


import com.example.moyiza_be.chat.dto.ChatRoomInfo;
import com.example.moyiza_be.chat.entity.Chat;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByIdIn(List<Long> chatIdList);

    Optional<Chat> findByRoomIdentifierAndChatType(Long roomIdentifier, ChatTypeEnum chatType);
}
