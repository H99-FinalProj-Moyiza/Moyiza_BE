package com.example.moyiza_be.chat.repository;

import com.example.moyiza_be.chat.entity.ChatJoinEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatJoinEntryRepository extends JpaRepository<ChatJoinEntry, Long> {
    List<ChatJoinEntry> findAllByUserIdAndIsCurrentlyJoinedTrue(Long id);
    Optional<ChatJoinEntry> findByUserIdAndChatIdAndIsCurrentlyJoinedTrue(Long userId, Long chatId);
    Optional<ChatJoinEntry> findByChatIdAndUserId(Long chatId, Long userId);
    Long countByChatIdAndAndIsCurrentlyJoinedTrue(Long chatId);
}
