package com.example.moyiza_be.like.service;


import com.example.moyiza_be.common.enums.LikeTypeEnum;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.like.entity.ClubLike;
import com.example.moyiza_be.like.entity.EventLike;
import com.example.moyiza_be.like.entity.OnedayLike;
import com.example.moyiza_be.like.repository.ClubLikeRepository;
import com.example.moyiza_be.like.repository.EventLikeRepository;
import com.example.moyiza_be.like.repository.OnedayLikeRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final ClubLikeRepository clubLikeRepository;
    private final EventLikeRepository eventLikeRepository;
    private final OnedayLikeRepository onedayLikeRepository;

    @Transactional
    public ResponseEntity<Message> clubLike(Long userId,Long clubId){
        if(checkLikeExists(userId, clubId, LikeTypeEnum.CLUB)){
            throw new DuplicateRequestException("중복으로 좋아요 할 수 없습니다");
        }
        ClubLike clubLike = new ClubLike(userId, clubId);
        clubLikeRepository.save(clubLike);
        return new ResponseEntity<>(new Message("좋아요 성공"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> cancelClubLike(Long userId, Long clubId){
        if(!checkLikeExists(userId, clubId, LikeTypeEnum.CLUB)){
            throw new NullPointerException("취소할 좋아요가 없습니다");
        }
        clubLikeRepository.deleteByUserIdAndClubId(userId, clubId);

        return new ResponseEntity<>(new Message("좋아요 취소"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> onedayLike(Long userId,Long onedayId){
        if(checkLikeExists(userId, onedayId, LikeTypeEnum.ONEDAY)){
            throw new DuplicateRequestException("중복으로 좋아요 할 수 없습니다");
        }
        OnedayLike onedayLike = new OnedayLike(userId, onedayId);
        onedayLikeRepository.save(onedayLike);
        return new ResponseEntity<>(new Message("좋아요 성공"), HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<Message> cancelOnedayLike(Long userId, Long onedayId){
        if(!checkLikeExists(userId, onedayId, LikeTypeEnum.ONEDAY)){
            throw new NullPointerException("취소할 좋아요가 없습니다");
        }
        onedayLikeRepository.deleteByUserIdAndOnedayId(userId, onedayId);

        return new ResponseEntity<>(new Message("좋아요 취소"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> eventLike(Long userId,Long eventId){
        if(checkLikeExists(userId, eventId, LikeTypeEnum.EVENT)){
            throw new DuplicateRequestException("중복으로 좋아요 할 수 없습니다");
        }
        EventLike eventLike = new EventLike(userId, eventId);
        eventLikeRepository.save(eventLike);
        return new ResponseEntity<>(new Message("좋아요 성공"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Message> cancelEventLike(Long userId, Long eventId){
        if(!checkLikeExists(userId, eventId, LikeTypeEnum.EVENT)){
            throw new NullPointerException("취소할 좋아요가 없습니다");
        }
        eventLikeRepository.deleteByUserIdAndEventId(userId, eventId);

        return new ResponseEntity<>(new Message("좋아요 취소"), HttpStatus.OK);
    }







    public Boolean checkLikeExists(Long userId, Long identifier, LikeTypeEnum likeTypeEnum){
        if(userId == null){return false;}
        switch(likeTypeEnum){
            case CLUB -> {
                return clubLikeRepository.existsByUserIdAndClubId(userId, identifier);
            }
            case ONEDAY -> {
                return onedayLikeRepository.existsByUserIdAndOnedayId(userId, identifier);
            }
            case EVENT -> {
                return eventLikeRepository.existsByUserIdAndEventId(userId, identifier);
            }
            default -> {
                throw new IllegalArgumentException("Unknown LikeType");
            }
        }
    }
}
