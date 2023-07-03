package com.example.moyiza_be.chat.repository;

import com.example.moyiza_be.chat.entity.ChatRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {
    Page<ChatRecord> findAllByChatIdAndCreatedAtAfter(Pageable pageable, Long chatId, LocalDateTime fromDate);
    Long countByChatIdEqualsAndIdGreaterThan(Long chatId, Long chatRecordId);

}
