package com.example.moyiza_be.blackList.service;

import com.example.moyiza_be.blackList.dto.BlackListMemberResponse;
import com.example.moyiza_be.blackList.entity.BlackList;
import com.example.moyiza_be.blackList.repository.BlackListRepository;
import com.example.moyiza_be.blackList.repository.QueryDSL.BlackListRepositoryCustom;
import com.example.moyiza_be.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
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

    public List<Long> filtering(User user) {
        List<BlackList> filteringList = blackListRepository.findAllByUserIdOrBlackListUserId(user.getId(), user.getId());
        //List of blacklisted IDs by the user
        List<Long> blackListIdList = filteringList.stream()
                .filter(blackList -> blackList.getUserId().equals(user.getId()))
                .map(BlackList::getBlackListUserId)
                .collect(Collectors.toList());

        //List of IDs that blacklisted the user
        List<Long> blackListedIdList = filteringList.stream()
                .filter(blackList -> blackList.getBlackListUserId().equals(user.getId()))
                .map(BlackList::getUserId)
                .collect(Collectors.toList());

        //List of IDs with duplicates removed
        return Stream.concat(blackListIdList.stream(), blackListedIdList.stream())
                .distinct().collect(Collectors.toList());
    }
}
