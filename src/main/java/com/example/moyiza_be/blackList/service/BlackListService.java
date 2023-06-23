package com.example.moyiza_be.blackList.service;

import com.example.moyiza_be.blackList.dto.BlackListMemberResponse;
import com.example.moyiza_be.blackList.entity.BlackList;
import com.example.moyiza_be.blackList.repository.BlackListRepository;
import com.example.moyiza_be.blackList.repository.QueryDSL.BlackListRepositoryCustom;
import com.example.moyiza_be.common.enums.BoardTypeEnum;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final BlackListRepositoryCustom blackListRepositoryCustom;

    public ResponseEntity<?> blackListing(User user, Long profileId) {
        Long userId = user.getId();
        if (userId.equals(profileId)) {
            throw new IllegalArgumentException("You can't blacklist yourself.");
        }

        BlackList blackList = blackListRepository.findByBlackListUserId(profileId);
        if (blackList == null) {
            BlackList newBlackList = new BlackList(userId, profileId);
            blackListRepository.save(newBlackList);
            return new ResponseEntity<>("BlackList registered.", HttpStatus.OK);
        } else {
            blackListRepository.delete(blackList);
            return new ResponseEntity<>("BlackList unlisted.", HttpStatus.OK);
        }
    }

    public ResponseEntity<List<BlackListMemberResponse>> getBlackList(User user) {
        List<BlackListMemberResponse> blackListMemberList = blackListRepositoryCustom.getBlackListMembers(user.getId());
        return new ResponseEntity<>(blackListMemberList, HttpStatus.OK);
    }

    public List<Long> blackListFiltering(User user, BoardTypeEnum boardTypeEnum) {
        if (user != null) {
            Long userId = user.getId();
            List<Long> blackUserIdList = blackListRepositoryCustom.getBlackUserIdList(userId);

            if (blackUserIdList.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<Long> blackListBoardIdList = switch (boardTypeEnum) {
                    case CLUB -> blackListRepositoryCustom.getBlackClubIdList(blackUserIdList);
                    case EVENT -> blackListRepositoryCustom.getBlackEventIdList(blackUserIdList);
                    case ONEDAY -> blackListRepositoryCustom.getBlackOneDayIdList(blackUserIdList);
                    case REVIEW -> blackListRepositoryCustom.getBlackReviewIdList(blackUserIdList);
                };
                log.info("blackUserIdList : " + blackUserIdList);
                log.info("blackListBoardIdList : " + boardTypeEnum + " / " + blackListBoardIdList.toString());
                return blackListBoardIdList;
            }
        } else {
            return Collections.emptyList();
        }
    }
}
