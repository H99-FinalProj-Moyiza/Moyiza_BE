package com.example.moyiza_be.blackList.repository;

import com.example.moyiza_be.blackList.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    BlackList findByBlackListUserId(Long profileId);

    List<BlackList> findAllByUserIdOrBlackListUserId(Long id, Long id2);
}
